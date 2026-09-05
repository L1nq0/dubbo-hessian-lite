/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.com.caucho.hessian.io.issue108;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.HessianProtocolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

public class ObjectDefinitionFieldCountTest {

    @Test
    void testHugeFieldCount() {
        byte[] bytes = {(byte) 0x43, 0x01, 0x78, 0x49,
                (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));

        Assertions.assertThrows(HessianProtocolException.class, input::readObject);
    }

    @Test
    void testFieldCountAtRemaining() {
        // 'C' 'x' count=6, then six empty field names
        byte[] bytes = {0x43, 0x01, 0x78, 0x49, 0x00, 0x00, 0x00, 0x06,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));

        Assertions.assertThrows(EOFException.class, input::readObject);
    }
}
