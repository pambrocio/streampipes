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

package org.streampipes.connect.adapter.generic.transform;

import java.util.List;
import java.util.Map;

public class DeleteTransformationRule implements TransformationRule {

    private List<String> key;

    public DeleteTransformationRule(List<String> key) {
        this.key = key;
    }

    @Override
    public Map<String, Object> transform(Map<String, Object> event) {
        return transform(event, key);
    }

    private Map<String, Object> transform(Map<String, Object> event, List<String> keys) {
        if (keys.size() == 1) {
            event.remove(keys.get(0));
            return event;
        } else {
            String key = keys.get(0);
            List<String> newKeysTmpList = keys.subList(1, keys.size());

            Map<String, Object> newSubEvent =
                    transform((Map<String, Object>) event.get(keys.get(0)), newKeysTmpList);

            event.remove(key);
            event.put(key, newSubEvent);
            return event;
        }

    }
}
