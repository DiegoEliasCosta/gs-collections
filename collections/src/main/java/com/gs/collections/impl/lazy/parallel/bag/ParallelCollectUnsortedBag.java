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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;

@Beta
class ParallelCollectUnsortedBag<T, V> extends AbstractParallelUnsortedBag<V>
{
    private final AbstractParallelUnsortedBag<T> parallelUnsortedBag;
    private final Function<? super T, ? extends V> function;

    ParallelCollectUnsortedBag(AbstractParallelUnsortedBag<T> parallelUnsortedBag, Function<? super T, ? extends V> function)
    {
        this.parallelUnsortedBag = parallelUnsortedBag;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.parallelUnsortedBag.forEach(Functions.bind(procedure, this.function));
    }

    public void forEachWithOccurrences(final ObjectIntProcedure<? super V> procedure)
    {
        this.parallelUnsortedBag.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int parameter)
            {
                procedure.value(ParallelCollectUnsortedBag.this.function.valueOf(each), parameter);
            }
        });
    }

    @Override
    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.parallelUnsortedBag.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    @Override
    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.parallelUnsortedBag.allSatisfy(Predicates.attributePredicate(this.function, predicate));
    }
}