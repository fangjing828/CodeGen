/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.codegen.java.views;

import static com.speedment.codegen.Formatting.*;
import com.speedment.codegen.base.CodeGenerator;
import com.speedment.codegen.base.CodeView;
import com.speedment.codegen.base.DependencyManager;
import com.speedment.codegen.lang.models.ClassOrInterface;
import com.speedment.codegen.lang.models.Constructor;
import com.speedment.codegen.lang.models.Field;
import com.speedment.codegen.lang.models.Method;
import java.util.Optional;
import com.speedment.util.CodeCombiner;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 *
 * @author Emil Forslund
 * @param <M>
 */
public abstract class ClassOrInterfaceView<M extends ClassOrInterface> implements CodeView<M> {
	protected final static String
		CLASS_STRING = "class ",
		INTERFACE_STRING = "interface ",
		ENUM_STRING = "enum ",
		IMPLEMENTS_STRING = "implements ",
		EXTENDS_STRING = "extends ",
		PACKAGE_STRING = "package ";
	
	private String renderPackage(M model) {
		Optional<String> pack = packageName(model.getName());
		if (pack.isPresent()) {
			return PACKAGE_STRING + pack.get() + scdnl();
		} else {
			return EMPTY;
		}
	}
	
	protected String onBeforeFields(CodeGenerator cg, M model) {
		return EMPTY;
	}
	
	protected Object wrapField(Field field) {return field;}
	protected Object wrapMethod(Method method) {return method;}
	protected String onAfterFields(CodeGenerator cg, M model) {return EMPTY;}
	
	private <In, C extends Collection<In>> Collection<Object> 
		wrap(C models, Function<In, Object> wrapper) {
		return models.stream().map(wrapper).collect(Collectors.toList());
	}
	
	protected abstract String classOrInterfaceLabel();
	protected abstract String extendsOrImplementsLabel();
	protected abstract String onSuperType(CodeGenerator cg, M model);

	@Override
	public Optional<String> render(CodeGenerator cg, M model) {
		Optional<String> packageName = packageName(model.getName());
		final DependencyManager mgr = cg.getDependencyMgr();
		mgr.clearDependencies();
		
		if (packageName.isPresent()) {
			if (mgr.isIgnored(packageName.get())) {
				packageName = Optional.empty();
			} else {
				mgr.ignorePackage(packageName.get());
			}
		}
		
		final Optional<String> view = Optional.of(renderPackage(model) +
			cg.onEach(model.getDependencies()).collect(CodeCombiner.joinIfNotEmpty(nl(), EMPTY, dnl())) +
			ifelse(cg.on(model.getJavadoc()), s -> s + nl(), EMPTY) +
			cg.onEach(model.getModifiers()).collect(CodeCombiner.joinIfNotEmpty(SPACE, EMPTY, SPACE)) +
			classOrInterfaceLabel() + shortName(model.getName()) + SPACE +
			onSuperType(cg, model) +
			cg.onEach(model.getInterfaces()).collect(CodeCombiner.joinIfNotEmpty(COMMA_SPACE, extendsOrImplementsLabel(), SPACE)) +
			block(
				onBeforeFields(cg, model) +
				cg.onEach(wrap(model.getFields(), (Field f) -> wrapField(f)))
					.collect(CodeCombiner.joinIfNotEmpty(scnl(), EMPTY, scdnl())) +
				onAfterFields(cg, model) +
				cg.onEach(wrap(model.getMethods(), (Method m) -> wrapMethod(m)))
					.collect(CodeCombiner.joinIfNotEmpty(dnl()))
			)
		);
		
		if (packageName.isPresent()) {
			mgr.acceptPackage(packageName.get());
		}
		
		return view;
	}
	
}