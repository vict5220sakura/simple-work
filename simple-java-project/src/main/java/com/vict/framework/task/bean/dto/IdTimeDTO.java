package com.vict.framework.task.bean.dto;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class IdTimeDTO {
    private String id;
    private Long actionTime;
    private Long overTimestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdTimeDTO idTimeDTO = (IdTimeDTO) o;
        return Objects.equals(id, idTimeDTO.id) && Objects.equals(actionTime, idTimeDTO.actionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionTime);
    }

    public static void main(String[] args) {
        ConcurrentHashMap<IdTimeDTO, IdTimeDTO> idTimeMap = new ConcurrentHashMap<IdTimeDTO, IdTimeDTO>();
        IdTimeDTO idTimeDTO = new IdTimeDTO();
        idTimeDTO.setId("001");
        idTimeDTO.setActionTime(111L);

        IdTimeDTO idTimeDTO1 = idTimeMap.putIfAbsent(idTimeDTO, idTimeDTO);
        System.out.println(idTimeDTO1);

        IdTimeDTO idTimeDTO2 = idTimeMap.putIfAbsent(idTimeDTO, idTimeDTO);
        System.out.println(idTimeDTO2);
    }
}
