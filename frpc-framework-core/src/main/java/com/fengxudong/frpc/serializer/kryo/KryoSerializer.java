package com.fengxudong.frpc.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.fengxudong.frpc.enums.SerializationTypeEnum;
import com.fengxudong.frpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author feng xud
 */
public class KryoSerializer implements Serializer {

    private static Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        protected Kryo create() {
            return initialRegister();
        }
    };

    private static Kryo initialRegister() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        return kryo;
    }

    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo = kryoPool.obtain();
        Output output = new Output(new ByteArrayOutputStream());
        try {
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        } finally {
            kryoPool.free(kryo);
            output.close();
            output.flush();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = kryoPool.obtain();
        Input input = new Input(new ByteArrayInputStream(bytes));
        try {
            return (T) kryo.readClassAndObject(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            kryoPool.free(kryo);
            input.close();
        }
    }

    @Override
    public byte getType() {
        return SerializationTypeEnum.KYRO.getCode();
    }
}
