<template>
    <div class="m-4 flex flex-row">
        <div class="mr-4" v-permission="permissionList.selectOrganize">
            <el-tree
                ref="treeRef"
                class="w-[300px]"
                node-key="id"
                :data="allOrganizeData"
                :props="defaultProps"
                :filter-node-method="filterNode"
                :expand-on-click-node="false"
                :highlight-current="true"
                :current-node-key="chooseOrganizeId"
                @node-click="handleOrganizeClick"
            />
        </div>
        <div>
            <!-- 操作栏 -->
            <div class="mb-10">
                <el-button @click="changeName" type="primary" v-permission="permissionList.changOrganizeName">修改组织名称</el-button>
                <el-button @click="addOrganize" type="primary" v-permission="permissionList.addOrganize">新增组织</el-button>
                <el-button @click="deleteOrganize" type="danger" v-permission="permissionList.deleteOrganize">删除组织</el-button>
            </div>
            <InnerDialog v-model="changeNameDialog">
                <template #header>修改组织名称</template>
                <template #default>
                    <el-input v-model="changeNameName" placeholder="请输入组织名称"></el-input>
                </template>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="changeNameCancel">取消</el-button>
                        <el-button type="primary" @click="changeNameSave">保存</el-button>
                    </div>
                </template>
            </InnerDialog>

            <InnerDialog v-model="addOrganizeDialog">
                <template #header>新增组织</template>
                <template #default>
                    <el-input v-model="addOrganizeName" placeholder="请输入组织名称"></el-input>
                </template>
                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="addOrganizeCancel">取消</el-button>
                        <el-button type="primary" @click="addOrganizeSave">保存</el-button>
                    </div>
                </template>
            </InnerDialog>
            <hr/>
            <div class="mt-10">
                <span>部门:&nbsp;{{chooseOrganize?.organizeName}}</span>
            </div>
            <!-- 添加用户 -->
            <div class="mt-4">
                <el-button type="primary" @click="needAddUser" v-permission="permissionList.organizeAddBuser">添加用户</el-button>
                <InnerDialog v-model="needAddUserDialog" :width="800" top="3vh">
                    <template #default>
                        <ChooseBuser ref="chooseBuserRef" :checkChoose="checkChoose">
                        
                        </ChooseBuser>
                    </template>
                    <template #footer>
                        <el-button @click="addUserCancel">取消</el-button>
                        <el-button type="primary" @click="addUserSave">确认</el-button>
                    </template>
                </InnerDialog>
                
            </div>
            <div>
                <BuserComponent 
                    ref="buserRef" 
                    v-model:organizeId="chooseOrganizeId"
                    @removeBuser="removeBuser"  
                    @removeBuserBatch="removeBuserBatch"
                    :showDeleteBtn="true" 
                ></BuserComponent>
            </div>

        </div>
    </div>
</template>

<script setup lang="ts" name="Organize">
import {useOrganizehooks} from '../organize/OrganizeHooks';
import InnerDialog from '@/components/utils/InnerDialog.vue';
import ChooseBuser from "@/components/buser/ChooseBuser.vue"
import BuserComponent from "@/components/buser/BuserComponent.vue"
import { useTemplateRef, ref } from 'vue';

let {
    permissionList,
    chooseOrganizeId,
    allOrganizeData,
    defaultProps,
    filterNode,
    handleOrganizeClick,
    changeName,
    changeNameDialog,
    changeNameName,
    changeNameCancel,
    changeNameSave,
    addOrganize,
    addOrganizeDialog,
    addOrganizeName,
    addOrganizeCancel,
    addOrganizeSave,
    deleteOrganize,
    chooseOrganize,
    needAddUser,
    needAddUserDialog,
    checkChoose,
    buserChooseOrganizeId,
    addUserCancel,
    addUserSave,
    removeBuser,
    removeBuserBatch
} = useOrganizehooks()

</script>

<style scoped></style>
./OrganizeHooks