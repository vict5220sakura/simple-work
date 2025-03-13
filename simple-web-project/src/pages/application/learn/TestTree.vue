<template>
    tree
    <el-input
        v-model="filterText"
        style="width: 240px"
        placeholder="Filter keyword"
    />
    <el-tree
        ref="treeRef"
        class="w-[600px]"
        :data="data"
        :props="defaultProps"
        @node-click="handleNodeClick"
        :filter-node-method="filterNode"
        :expand-on-click-node="false"
        :highlight-current="true"
    />
    <el-button @click="add">添加</el-button>
</template>

<script setup lang="ts" name="TestTree">
import {randomId} from '@/utils/IdUtils'
import { ref, watch } from 'vue'

const filterText = ref('')
const treeRef = ref<any>()

watch(filterText, (val) => {
  treeRef.value!.filter(val)
})

interface Tree {
    id: string,
    label: string
    children?: Tree[]
}

const filterNode = (value: string, data: Tree) => {
    if (!value) return true
    return data.label.includes(value)
}

let chooseData = ref<Tree>()
const handleNodeClick = (data: Tree) => {
    // console.log({...data})
    chooseData.value = data;
}

let data = ref<Tree[]>([
  {
    id: randomId(),
    label: 'Level one 1',
    children: [
      {
        id: randomId(),
        label: 'Level two 1-1',
        children: [
          {
            id: randomId(),
            label: 'Level three 1-1-1',
          },
        ],
      },
    ],
  },
  {
    id: randomId(),
    label: 'Level one 2',
    children: [
      {
        id: randomId(),
        label: 'Level two 2-1',
        children: [
          {
            id: randomId(),
            label: 'Level three 2-1-1',
          },
        ],
      },
      {
        id: randomId(),
        label: 'Level two 2-2',
        children: [
          {
            id: randomId(),
            label: 'Level three 2-2-1',
          },
        ],
      },
    ],
  },
  {
    id: randomId(),
    label: 'Level one 3',
    children: [
      {
        id: randomId(),
        label: 'Level two 3-1',
        children: [
          {
            id: randomId(),
            label: 'Level three 3-1-1',
          },
        ],
      },
      {
        id: randomId(),
        label: 'Level two 3-2',
        children: [
          {
            id: randomId(),
            label: 'Level three 3-2-1',
          },
        ],
      },
    ],
  },
])

const defaultProps = {
    label: 'label',
    children: 'children',
}

const add = ()=>{
    if(chooseData.value){
        if(!chooseData.value?.children){
            chooseData.value.children = []
        }
        let addData:Tree = {
            id: randomId(),
            label: randomId()
        }
        chooseData.value.children.push(addData)
    }
    
}

</script>

<style scoped></style>
