/*
    Copyright 2013 Rustici Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.rusticisoftware.tincan;

import static com.rusticisoftware.tincan.TestUtils.assertSerializeDeserialize;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActivityTest {
    @Test
    public void testGetObjectType() throws Exception {
        Activity mock = new Activity();
        assertEquals(mock.getObjectType(), "Activity");
    }

    @Test
    public void serializeDeserialize() throws Exception {
        Activity act = new Activity();
        act.setId("http://example.com/activity");
        
        ActivityDefinition def = new ActivityDefinition();
        def.setName(new LanguageMap());
        def.getName().put("en-US", "Example Activity");
        act.setDefinition(def);
        
        assertSerializeDeserialize(act);
    }
    
    @Test
    public void testGetId() throws Exception {
        String id = "http://example.com/activity";
        Activity act = new Activity(id);
        assertEquals(id, act.getId().toString());
    }
}
