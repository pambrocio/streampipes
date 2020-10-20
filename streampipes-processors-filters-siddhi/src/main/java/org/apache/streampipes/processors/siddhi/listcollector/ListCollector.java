/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.streampipes.processors.siddhi.listcollector;

import org.apache.streampipes.commons.exceptions.SpRuntimeException;
import org.apache.streampipes.container.api.ResolvesContainerProvidedOutputStrategy;
import org.apache.streampipes.model.DataProcessorType;
import org.apache.streampipes.model.graph.DataProcessorDescription;
import org.apache.streampipes.model.graph.DataProcessorInvocation;
import org.apache.streampipes.model.schema.*;
import org.apache.streampipes.sdk.builder.ProcessingElementBuilder;
import org.apache.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.apache.streampipes.sdk.extractor.ProcessingElementParameterExtractor;
import org.apache.streampipes.sdk.helpers.*;
import org.apache.streampipes.sdk.utils.Assets;
import org.apache.streampipes.sdk.utils.Datatypes;
import org.apache.streampipes.wrapper.siddhi.model.SiddhiProcessorParams;
import org.apache.streampipes.wrapper.siddhi.engine.StreamPipesSiddhiProcessor;
import org.apache.streampipes.wrapper.standalone.ProcessorParams;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static org.apache.streampipes.wrapper.siddhi.utils.SiddhiUtils.prepareName;

public class ListCollector extends StreamPipesSiddhiProcessor
        implements ResolvesContainerProvidedOutputStrategy<DataProcessorInvocation, ProcessingElementParameterExtractor> {

  private static final String LIST_KEY = "list-key";
  private static final String TIME_WINDOW_SIZE = "time-window-size";

  @Override
  public DataProcessorDescription declareModel() {
    return ProcessingElementBuilder.create("org.apache.streampipes.processors.siddhi.listcollector")
            .withLocales(Locales.EN)
            .category(DataProcessorType.PATTERN_DETECT)
            .withAssets(Assets.DOCUMENTATION, Assets.ICON)
            .requiredStream(StreamRequirementsBuilder.create()
                    .requiredPropertyWithUnaryMapping(EpRequirements.anyProperty(), Labels.withId
                            (LIST_KEY), PropertyScope.MEASUREMENT_PROPERTY)
                    .build())
            .requiredIntegerParameter(Labels.withId(TIME_WINDOW_SIZE))
            .outputStrategy(OutputStrategies.customTransformation())
            .build();

  }

  @Override
  public String fromStatement(SiddhiProcessorParams<ProcessorParams> siddhiParams) {
    return "from " +siddhiParams.getInputStreamNames().get(0) + "#window.lengthBatch(10)";
  }

  @Override
  public String selectStatement(SiddhiProcessorParams<ProcessorParams> siddhiParams) {
    String propertySelector = siddhiParams.getParams().extractor().mappingPropertyValue(LIST_KEY);
    StringJoiner joiner = new StringJoiner(",");
    siddhiParams.getEventTypeInfo().forEach((key, value) -> value.forEach(field -> joiner.add(field.getSelectorPrefix() + field.getFieldName())));
    return "select " +joiner.toString() +", list:collect(" + prepareName(propertySelector) +") as " + prepareName(propertySelector) + "_list";

  }

  @Override
  public EventSchema resolveOutputStrategy(DataProcessorInvocation processingElement, ProcessingElementParameterExtractor extractor) throws SpRuntimeException {
    String propertySelector = extractor.mappingPropertyValue(LIST_KEY);
    EventSchema inputSchema = processingElement.getInputStreams().get(0).getEventSchema();

    List<EventProperty> selectedProperty = extractor
            .getInputStreamEventPropertySubset(Collections.singletonList(propertySelector));

    if (selectedProperty.size() > 0) {
      EventPropertyPrimitive prop = (EventPropertyPrimitive) selectedProperty.get(0);
      String newPropertyName = prop.getRuntimeName() + "_list";
      String newDatatype = prop.getRuntimeType();
      String newDomainProperty = prop.getDomainProperties().get(0).toString();

      EventPropertyList ep = EpProperties.listEp(Labels.from("list", "", ""),
              newPropertyName,
              Datatypes.fromDatatypeString(newDatatype),
              newDomainProperty);

      inputSchema.addEventProperty(ep);
    }

    return inputSchema;
  }
}
