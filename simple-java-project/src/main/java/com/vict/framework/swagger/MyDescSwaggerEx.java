package com.vict.framework.swagger;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.fastjsonserializer.EnumValueSerializer;
import com.vict.framework.myannotation.MySwaggerDesc;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.util.Optional;

@Component
@Order
public class MyDescSwaggerEx implements ModelPropertyBuilderPlugin {


    @Override
    public void apply(ModelPropertyContext modelPropertyContext) {
        String name = Optional.ofNullable(modelPropertyContext).map(o-> o.getBeanPropertyDefinition()).map(o-> o.get()).map(o-> o.getName()).orElse("");

        String myDesc = Optional.ofNullable(modelPropertyContext).map(o -> o.getBeanPropertyDefinition()).map(o -> o.get()).map(o -> o.getField())
                .map(o -> o.getAnnotated()).map(o -> o.getAnnotation(MySwaggerDesc.class)).map(o -> o.value()).orElse(null);

        if(myDesc == null){
            myDesc = Optional.ofNullable(modelPropertyContext).map(o -> o.getBeanPropertyDefinition()).map(o -> o.get()).map(o -> o.getGetter())
                    .map(o -> o.getAnnotated()).map(o -> o.getAnnotation(MySwaggerDesc.class)).map(o -> o.value()).orElse(null);
        }

        Class<?> serializeUsingClass = Optional.ofNullable(modelPropertyContext).map(o -> o.getBeanPropertyDefinition()).map(o -> o.get()).map(o -> o.getField())
                .map(o -> o.getAnnotated()).map(o -> o.getAnnotation(JSONField.class)).map(o -> o.serializeUsing()).orElse(null);

        if(serializeUsingClass == null){
            serializeUsingClass = Optional.ofNullable(modelPropertyContext).map(o -> o.getBeanPropertyDefinition()).map(o -> o.get()).map(o -> o.getGetter())
                    .map(o -> o.getAnnotated()).map(o -> o.getAnnotation(JSONField.class)).map(o -> o.serializeUsing()).orElse(null);
        }

        String newName = null;
        if(serializeUsingClass != null && (serializeUsingClass.equals(EnumValueSerializer.class) || serializeUsingClass.equals(EnumSerializer.class))){
            newName = name + "    " + "|" + name + "Name";
        }

        if(myDesc != null){
            if(newName == null){
                newName = name + "    " + myDesc;
            }else{
                newName = newName + "    " + myDesc;
            }
        }

        if(newName != null){
            modelPropertyContext.getBuilder().name(newName);
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
