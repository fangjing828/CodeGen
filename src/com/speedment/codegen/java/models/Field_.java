/*
 * Copyright 2015 Emil Forslund.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.speedment.codegen.java.models;

import com.speedment.codegen.base.CodeModel;
import com.speedment.codegen.java.interfaces.Documentable;
import com.speedment.codegen.java.interfaces.Nameable;
import com.speedment.codegen.java.interfaces.Typeable;
import com.speedment.codegen.java.models.modifiers.FieldModifier;
import com.speedment.codegen.java.models.modifiers.Modifier_;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Emil Forslund
 */
public class Field_ implements CodeModel<Field_>, 
		Nameable<Field_>, 
		Typeable<Field_>, 
		Documentable<Field_>, 
		FieldModifier<Field_> {
	
	private CharSequence name;
	private Type_ type;
	private Optional<Javadoc_> javadoc = Optional.empty();
	private final Set<Modifier_> modifiers = EnumSet.noneOf(Modifier_.class);
	
	public Field_(CharSequence name, Type_ type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public CharSequence getName() {
		return name;
	}

	@Override
	public Field_ setName(CharSequence name) {
		this.name = name;
		return this;
	}

	@Override
	public Type_ getType() {
		return type;
	}

	@Override
	public Field_ setType(Type_ type) {
		this.type = type;
		return this;
	}

	@Override
	public Field_ clone() {
		return new Field_(name.toString(), type);
	}
	
	@Override
	public Set<Modifier_> getModifiers() {
		return modifiers;
	}

	@Override
	public Field_ setJavadoc(Javadoc_ doc) {
		javadoc = Optional.of(doc);
		return this;
	}

	@Override
	public Optional<Javadoc_> getJavadoc() {
		return javadoc;
	}
}