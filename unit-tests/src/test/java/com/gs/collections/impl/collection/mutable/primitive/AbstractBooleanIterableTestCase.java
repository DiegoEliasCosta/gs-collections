/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.collection.mutable.primitive;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link BooleanIterable}s.
 */
public abstract class AbstractBooleanIterableTestCase
{
    protected abstract BooleanIterable classUnderTest();

    protected abstract BooleanIterable getEmptyCollection();

    protected abstract BooleanIterable getEmptyMutableCollection();

    protected abstract MutableCollection<Boolean> getEmptyObjectCollection();

    protected abstract BooleanIterable newWith(boolean... elements);

    protected abstract BooleanIterable newMutableCollectionWith(boolean... elements);

    protected abstract MutableCollection<Object> newObjectCollectionWith(Object... elements);

    @Test
    public void newCollectionWith()
    {
        BooleanIterable iterable = this.classUnderTest();
        Verify.assertSize(3, iterable);
        Assert.assertTrue(iterable.containsAll(true, false, true));
    }

    @Test
    public void newCollection()
    {
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true), this.classUnderTest());
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.getEmptyCollection());
        Verify.assertNotEmpty(this.classUnderTest());
        Verify.assertNotEmpty(this.newWith(false));
        Verify.assertNotEmpty(this.newWith(true));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.getEmptyCollection().notEmpty());
        Assert.assertTrue(this.classUnderTest().notEmpty());
        Assert.assertTrue(this.newWith(false).notEmpty());
        Assert.assertTrue(this.newWith(true).notEmpty());
    }

    @Test
    public void contains()
    {
        BooleanIterable emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.contains(true));
        Assert.assertFalse(emptyCollection.contains(false));
        Assert.assertTrue(this.classUnderTest().contains(true));
        Assert.assertTrue(this.classUnderTest().contains(false));
        Assert.assertFalse(this.newWith(true, true, true).contains(false));
        Assert.assertFalse(this.newWith(false, false, false).contains(true));
    }

    @Test
    public void containsAllArray()
    {
        BooleanIterable iterable = this.classUnderTest();
        Assert.assertTrue(iterable.containsAll(true));
        Assert.assertTrue(iterable.containsAll(true, false, true));
        Assert.assertTrue(iterable.containsAll(true, false));
        Assert.assertTrue(iterable.containsAll(true, true));
        Assert.assertTrue(iterable.containsAll(false, false));
        BooleanIterable emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.containsAll(true));
        Assert.assertFalse(emptyCollection.containsAll(false));
        Assert.assertFalse(emptyCollection.containsAll(false, true, false));
        Assert.assertFalse(this.newWith(true, true).containsAll(false, true, false));

        BooleanIterable trueCollection = this.newWith(true, true, true, true);
        Assert.assertFalse(trueCollection.containsAll(true, false));
        BooleanIterable falseCollection = this.newWith(false, false, false, false);
        Assert.assertFalse(falseCollection.containsAll(true, false));
    }

    @Test
    public void containsAllIterable()
    {
        BooleanIterable emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.containsAll(new BooleanArrayList()));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(false)));
        BooleanIterable iterable = this.newWith(true, true, false, false, false);
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(false)));
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertTrue(iterable.containsAll(BooleanArrayList.newListWith(true, false, true)));
        Assert.assertFalse(this.newWith(true, true).containsAll(BooleanArrayList.newListWith(false, true, false)));

        BooleanIterable trueCollection = this.newWith(true, true, true, true);
        Assert.assertFalse(trueCollection.containsAll(BooleanArrayList.newListWith(true, false)));
        BooleanIterable falseCollection = this.newWith(false, false, false, false);
        Assert.assertFalse(falseCollection.containsAll(BooleanArrayList.newListWith(true, false)));
    }

    @Test
    public abstract void booleanIterator();

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanIterator iterator = this.classUnderTest().booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }

        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_non_empty_collection()
    {
        BooleanIterable iterable = this.newWith(true, true, true);
        BooleanIterator iterator = iterable.booleanIterator();
        while (iterator.hasNext())
        {
            Assert.assertTrue(iterator.next());
        }
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_emptyList()
    {
        this.getEmptyCollection().booleanIterator().next();
    }

    @Test
    public void forEach()
    {
        final long[] sum = new long[1];
        this.classUnderTest().forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each ? 1 : 0;
            }
        });

        Assert.assertEquals(2L, sum[0]);

        final long[] sum1 = new long[1];
        this.newWith(true, false, false, true, true, true).forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum1[0] += each ? 1 : 2;
            }
        });

        Assert.assertEquals(8L, sum1[0]);
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, this.getEmptyCollection());
        Verify.assertSize(1, this.newWith(true));
        Verify.assertSize(1, this.newWith(false));
        Verify.assertSize(2, this.newWith(true, false));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.newWith(true, false, true).count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.getEmptyCollection().count(BooleanPredicates.isFalse()));

        BooleanIterable iterable = this.newWith(true, false, false, true, true, true);
        Assert.assertEquals(4L, iterable.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(2L, iterable.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(6L, iterable.count(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.classUnderTest().anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.newWith(true, true).anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.getEmptyCollection().anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(true).anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.newWith(false).anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(false, false, false).anySatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.classUnderTest().allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(false, false).allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.newWith(true, false).allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(true, true, true).allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(false, false, false).allSatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(this.classUnderTest().noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(false, false).noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(true, true).noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.newWith(true, true).noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(false, false, false).noneSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void select()
    {
        BooleanIterable iterable = this.classUnderTest();
        Verify.assertSize(2, iterable.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, iterable.select(BooleanPredicates.isFalse()));

        BooleanIterable iterable1 = this.newWith(false, true, false, false, true, true, true);
        Assert.assertEquals(this.newWith(true, true, true, true), iterable1.select(BooleanPredicates.isTrue()));
        Assert.assertEquals(this.newWith(false, false, false), iterable1.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        BooleanIterable iterable = this.classUnderTest();
        Verify.assertSize(1, iterable.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(2, iterable.reject(BooleanPredicates.isFalse()));

        BooleanIterable iterable1 = this.newWith(false, true, false, false, true, true, true);
        Assert.assertEquals(this.newWith(false, false, false), iterable1.reject(BooleanPredicates.isTrue()));
        Assert.assertEquals(this.newWith(true, true, true, true), iterable1.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        BooleanIterable iterable = this.classUnderTest();
        Assert.assertFalse(iterable.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(iterable.detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));

        BooleanIterable iterable1 = this.newWith(true, true, true);
        Assert.assertFalse(iterable1.detectIfNone(BooleanPredicates.isFalse(), false));
        Assert.assertTrue(iterable1.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(iterable1.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(iterable1.detectIfNone(BooleanPredicates.isTrue(), true));

        BooleanIterable iterable2 = this.newWith(false, false, false);
        Assert.assertTrue(iterable2.detectIfNone(BooleanPredicates.isTrue(), true));
        Assert.assertFalse(iterable2.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(iterable2.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(iterable2.detectIfNone(BooleanPredicates.isFalse(), false));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(this.newObjectCollectionWith(1, 0, 1), this.classUnderTest().collect(new BooleanToObjectFunction<Object>()
        {
            public Object valueOf(boolean value)
            {
                return Integer.valueOf(value ? 1 : 0);
            }
        }));

        BooleanToObjectFunction<Boolean> booleanToObjectFunction = new BooleanToObjectFunction<Boolean>()
        {
            public Boolean valueOf(boolean parameter)
            {
                return !parameter;
            }
        };
        Assert.assertEquals(this.newObjectCollectionWith(false, true, false), this.classUnderTest().collect(booleanToObjectFunction));
        Assert.assertEquals(this.getEmptyObjectCollection(), this.getEmptyCollection().collect(booleanToObjectFunction));
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(0L, this.getEmptyCollection().toArray().length);
        Assert.assertTrue(Arrays.equals(new boolean[]{false, true}, this.newWith(true, false).toArray())
                || Arrays.equals(new boolean[]{true, false}, this.newWith(true, false).toArray()));
    }

    @Test
    public void testEquals()
    {
        BooleanIterable iterable1 = this.newWith(true, false, true, false);
        BooleanIterable iterable2 = this.newWith(true, false, true, false);
        BooleanIterable iterable3 = this.newWith(false, false, false, true);
        BooleanIterable iterable4 = this.newWith(true, true, true);
        BooleanIterable iterable5 = this.newWith(true, true, false, false, false);

        Verify.assertEqualsAndHashCode(iterable1, iterable2);
        Verify.assertEqualsAndHashCode(this.getEmptyCollection(), this.getEmptyCollection());
        Verify.assertPostSerializedEqualsAndHashCode(iterable1);
        Verify.assertPostSerializedEqualsAndHashCode(iterable5);
        Verify.assertPostSerializedEqualsAndHashCode(this.getEmptyCollection());
        Assert.assertNotEquals(iterable1, iterable3);
        Assert.assertNotEquals(iterable1, iterable4);
        Assert.assertNotEquals(this.getEmptyCollection(), this.newWith(true));
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(this.getEmptyObjectCollection().hashCode(), this.getEmptyCollection().hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(true, false, true).hashCode(),
                this.newWith(true, false, true).hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(true).hashCode(),
                this.newWith(true).hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(false).hashCode(),
                this.newWith(false).hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.getEmptyCollection().toString());
        Assert.assertEquals("[true]", this.newWith(true).toString());
        BooleanIterable iterable = this.newWith(true, false);
        Assert.assertTrue("[true, false]".equals(iterable.toString())
                || "[false, true]".equals(iterable.toString()));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("true", this.newWith(true).makeString("/"));
        Assert.assertEquals("", this.getEmptyCollection().makeString());
        BooleanIterable iterable = this.newWith(true, false);
        Assert.assertTrue("true, false".equals(iterable.makeString())
                || "false, true".equals(iterable.makeString()));
        Assert.assertTrue(iterable.makeString("/"), "true/false".equals(iterable.makeString("/"))
                || "false/true".equals(iterable.makeString("/")));
        Assert.assertTrue(iterable.makeString("[", "/", "]"), "[true/false]".equals(iterable.makeString("[", "/", "]"))
                || "[false/true]".equals(iterable.makeString("[", "/", "]")));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        this.getEmptyCollection().appendString(appendable);
        Assert.assertEquals("", appendable.toString());
        StringBuilder appendable1 = new StringBuilder();
        this.newWith(true).appendString(appendable1);
        Assert.assertEquals("true", appendable1.toString());
        StringBuilder appendable2 = new StringBuilder();
        BooleanIterable iterable = this.newWith(true, false);
        iterable.appendString(appendable2);
        Assert.assertTrue("true, false".equals(appendable2.toString())
                || "false, true".equals(appendable2.toString()));
        StringBuilder appendable3 = new StringBuilder();
        iterable.appendString(appendable3, "/");
        Assert.assertTrue("true/false".equals(appendable3.toString())
                || "false/true".equals(appendable3.toString()));
        StringBuilder appendable4 = new StringBuilder();
        iterable.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(iterable.toString(), appendable4.toString());
    }

    @Test
    public void toList()
    {
        BooleanIterable iterable = this.newWith(true, false);
        Assert.assertTrue(BooleanArrayList.newListWith(false, true).equals(iterable.toList())
                || BooleanArrayList.newListWith(true, false).equals(iterable.toList()));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false, true), this.classUnderTest().toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.newWith(true, false, false, true, true, true).toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), this.classUnderTest().toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, true, true, true, true), this.newWith(true, false, false, true, true, true).toBag());
    }

    @Test
    public void asLazy()
    {
        BooleanIterable iterable = this.classUnderTest();
        Assert.assertEquals(iterable.toBag(), iterable.asLazy().toBag());
        Verify.assertInstanceOf(LazyBooleanIterable.class, iterable.asLazy());
    }
}
