package com.vict.controller.buser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vict.bean.organize.ao.*;
import com.vict.bean.organize.vo.CheckChooseVO;
import com.vict.bean.organize.vo.OrganizeVO;
import com.vict.entity.Organize;
import com.vict.entity.OrganizeBuser;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.OrganizeBuserMapper;
import com.vict.mapper.OrganizeMapper;
import com.vict.mapperservice.OrganizeBuserMapperService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/organize")
public class OrganizeController {

    @Autowired
    private OrganizeMapper organizeMapper;

    @Autowired
    private OrganizeBuserMapper organizeBuserMapper;

    @Autowired
    private OrganizeBuserMapperService organizeBuserMapperService;

    @PostMapping("/selectAllOrganize")
    public R<List<OrganizeVO>> selectAllOrganize(){
        List<Organize> list = organizeMapper.selectAll();

        OrganizeVO organizeVO = new OrganizeVO();

        Organize organize = list.get(0);

        organizeVO.setId(organize.getId());
        organizeVO.setFatherId(null);
        organizeVO.setOrganizeName(organize.getOrganizeName());

        HashMap<Long, OrganizeVO> id_organizeVO = new HashMap<>();
        id_organizeVO.put(organizeVO.getId(), organizeVO);

        for(int i = 1 ; i < list.size() ; i++){
            Organize organizeTemp = list.get(i);
            Long fatherId = organizeTemp.getFatherId();
            OrganizeVO organizeVOFather = id_organizeVO.get(fatherId);
            if(organizeVOFather == null){
                continue;
            }

            OrganizeVO organizeVOTemp = new OrganizeVO();
            organizeVOTemp.setId(organizeTemp.getId());
            organizeVOTemp.setOrganizeName(organizeTemp.getOrganizeName());
            organizeVOTemp.setFatherId(organizeTemp.getFatherId());

            if(organizeVOFather.getChildren() == null){
                organizeVOFather.setChildren(new ArrayList<>());
            }
            organizeVOFather.getChildren().add(organizeVOTemp);

            id_organizeVO.put(organizeVOTemp.getId(), organizeVOTemp);
        }

        List<OrganizeVO> listVO = new ArrayList<>();
        listVO.add(organizeVO);
        return R.ok(listVO);
    }

    @PostMapping("/changeName")
    public R changeName(@RequestBody ChangeNameAO changeNameAO){
        Organize organize = organizeMapper.selectById(changeNameAO.getId());
        organize.setOrganizeName(changeNameAO.getOrganizeName());
        organizeMapper.updateById(organize);
        return R.ok();
    }

    @PostMapping("/addOrganize")
    public R<OrganizeVO> addOrganize(@RequestBody AddOrganizeAO addOrganizeAO){
        Organize organize = new Organize();
        organize.setId(IdUtils.snowflakeId());
        organize.setFatherId(addOrganizeAO.getFatherId());
        organize.setOrganizeName(addOrganizeAO.getOrganizeName());
        organizeMapper.insert(organize);

        OrganizeVO organizeVO = new OrganizeVO();
        organizeVO.setId(organize.getId());
        organizeVO.setOrganizeName(organize.getOrganizeName());
        organizeVO.setFatherId(organize.getFatherId());
        return R.ok(organizeVO);
    }

    @PostMapping("/deleteOrganize")
    public R deleteOrganize(@RequestBody DeleteOrganizeAO deleteOrganizeAO){
        if(deleteOrganizeAO.getId() == 1){
            throw new RuntimeException("不能删除根节点");
        }
        organizeMapper.deleteById(deleteOrganizeAO.getId());
        return R.ok();
    }

    @PostMapping("/checkChoose")
    public R<CheckChooseVO> checkChoose(@RequestBody CheckChooseAO checkChooseAO){
        CheckChooseVO checkChooseVO = new CheckChooseVO();

        List<Long> ids = checkChooseAO.getIds();
        ids = Optional.ofNullable(ids).filter(o -> o != null).filter(o -> o.size() > 0).orElse(null);
        if(ids == null){
            checkChooseVO.setMap(new HashMap<>());
            return R.ok(checkChooseVO);
        }

        List<OrganizeBuser> organizeBusers = organizeBuserMapper.selectList(
                new LambdaQueryWrapper<OrganizeBuser>()
                        .eq(OrganizeBuser::getOrganizeId, checkChooseAO.getOrganizeId())
                        .in(OrganizeBuser::getBuserId, ids)
        );
        organizeBusers = Optional.ofNullable(organizeBusers).orElse(new ArrayList<>());

        List<Long> buserIds = organizeBusers.stream().map(o -> o.getBuserId()).collect(Collectors.toList());

        HashMap<String, String> map = new HashMap<>();

        for(Long id : ids){
            if(buserIds.contains(id)){
                map.put(id.toString(), CheckChooseVO.Choosed.choosed.getValue());
            }else{
                map.put(id.toString(), CheckChooseVO.Choosed.not.getValue());
            }
        }

        checkChooseVO.setMap(map);
        return R.ok(checkChooseVO);
    }

    @PostMapping("/addOrganizeChoose")
    public R addOrganizeChoose(@RequestBody AddOrganizeChooseAO addOrganizeChooseAO){
        Long organizeId = addOrganizeChooseAO.getOrganizeId();
        List<Long> buserIds = Optional.ofNullable(addOrganizeChooseAO).map(o -> o.getIds()).filter(o -> o.size() > 0)
                .orElse(null);
        if(buserIds == null){
            return R.ok();
        }
        List<OrganizeBuser> organizeBusers = new ArrayList<>();
        for(Long buserId : buserIds){
            OrganizeBuser organizeBuser = new OrganizeBuser();
            organizeBuser.setId(IdUtils.snowflakeId());
            organizeBuser.setOrganizeId(organizeId);
            organizeBuser.setBuserId(buserId);

            organizeBusers.add(organizeBuser);
        }

        organizeBuserMapperService.saveBatch(organizeBusers);
        return R.ok();
    }

    @PostMapping("/removeOrganizeChoose")
    public R removeOrganizeChoose(@RequestBody RemoveOrganizeChooseAO removeOrganizeChooseAO){
        List<OrganizeBuser> organizeBusers = organizeBuserMapper.selectList(
                new LambdaQueryWrapper<OrganizeBuser>()
                        .eq(OrganizeBuser::getOrganizeId, removeOrganizeChooseAO.getOrganizeId())
                        .eq(OrganizeBuser::getBuserId, removeOrganizeChooseAO.getBuserId())
        );
        for(OrganizeBuser organizeBuser : organizeBusers){
            organizeBuserMapper.deleteById(organizeBuser.getId());
        }
        return R.ok();
    }

    @PostMapping("/removeOrganizeChooseBatch")
    public R removeOrganizeChooseBatch(@RequestBody RemoveOrganizeChooseBatchAO removeOrganizeChooseBatchAO){
        List<OrganizeBuser> organizeBusers = organizeBuserMapper.selectList(
                new LambdaQueryWrapper<OrganizeBuser>()
                        .eq(OrganizeBuser::getOrganizeId, removeOrganizeChooseBatchAO.getOrganizeId())
                        .in(OrganizeBuser::getBuserId, removeOrganizeChooseBatchAO.getBuserIds())
        );
        for(OrganizeBuser organizeBuser : organizeBusers){
            organizeBuserMapper.deleteById(organizeBuser.getId());
        }
        return R.ok();
    }
}
