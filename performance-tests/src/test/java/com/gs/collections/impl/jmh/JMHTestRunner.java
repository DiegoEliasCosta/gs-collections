/*
 * Copyright 2014 Goldman Sachs.
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
 */

package com.gs.collections.impl.jmh;

import com.gs.collections.impl.JMHTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.runner.parameters.TimeValue;

public class JMHTestRunner
{
    @Test
    @Category(JMHTests.class)
    public void runTests() throws Exception
    {
        int warmupCount = 50;
        int runCount = 25;
        Options opts = new OptionsBuilder()
                .include(".*com.gs.collections.impl.jmh.*")
                .warmupTime(TimeValue.seconds(2))
                .warmupIterations(warmupCount)
                .measurementTime(TimeValue.seconds(2))
                .measurementIterations(runCount)
                .verbosity(VerboseMode.NORMAL)
                .forks(1)
                .build();

        new Runner(opts).run();
    }
}
