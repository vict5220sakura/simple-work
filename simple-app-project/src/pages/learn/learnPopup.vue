<template>
  <uv-popup ref="popup" mode="top" :close-on-click-overlay="false">
			<view style="width: 600rpx;">
				<text>丙辰中秋，欢饮达旦，大醉，作此篇，兼怀子由。 明月几时有？把酒问青天。
				不知天上宫阙，今夕是何年。我欲乘风归去，又恐琼楼玉宇，高处不胜寒。起舞弄清影，何似在人间。
				转朱阁，低绮户，照无眠。不应有恨，何事长向别时圆？人有悲欢离合，月有阴晴圆缺，此事古难全。
				但愿人长久，千里共婵娟。</text>
				<button @click="close">关闭</button>
			</view>
		</uv-popup>
		<button @click="open">打开</button>
</template>

<script setup lang="ts">
import {ref} from "vue"

const popup = ref<any>(null)

const open = ()=>{
  popup.value.open()
}

const close = ()=>{
  popup.value.close()
}
</script>

<style scoped></style>