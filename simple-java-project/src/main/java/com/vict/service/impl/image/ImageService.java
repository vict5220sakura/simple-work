package com.vict.service.impl.image;

import com.vict.entity.Image;
import com.vict.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Transactional
    public void changeImage(List<Long> ids){
        List<Image> images = imageMapper.selectBatchForUpdate(ids);
        List<Integer> orderNum = images.stream().map(o -> o.getOrderNum()).collect(Collectors.toList());
        orderNum.sort(Comparator.naturalOrder());
        Map<Long, Image> id_iamge = images.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));
        List<Image> images2 = new ArrayList<>(images.size());
        for(int i = 0 ; i < images.size() ; i++){
            Integer imageOldNum = orderNum.get(i);
            Long id = ids.get(i);
            Image imageNewNum = id_iamge.get(id);
            imageNewNum.setOrderNum(imageOldNum);
            images2.add(imageNewNum);
        }

        imageMapper.moveBatch(images2);
    }
}
