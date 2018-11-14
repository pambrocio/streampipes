/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.model.connect.rules;

import org.streampipes.empire.annotations.Namespaces;
import org.streampipes.empire.annotations.RdfProperty;
import org.streampipes.empire.annotations.RdfsClass;

import javax.persistence.Entity;

@Namespaces({"sp", "https://streampipes.org/vocabulary/v1/"})
@RdfsClass("sp:CreateNestedRuleDescription")
@Entity
public class CreateNestedRuleDescription extends TransformationRuleDescription {
    @RdfProperty("sp:runtimeKey")
    private String runtimeKey;

    public CreateNestedRuleDescription() {
       super();
    }

    public CreateNestedRuleDescription(String runtimeKey) {
        super();
        this.runtimeKey = runtimeKey;
    }

    public CreateNestedRuleDescription(CreateNestedRuleDescription other) {
        super(other);
        this.runtimeKey = other.getRuntimeKey();
    }

    public String getRuntimeKey() {
        return runtimeKey;
    }

    public void setRuntimeKey(String runtimeKey) {
        this.runtimeKey = runtimeKey;
    }
}
