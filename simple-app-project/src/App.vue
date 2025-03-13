<script setup lang="ts">
import { onLaunch } from "@dcloudio/uni-app";
import {init} from "./config/Common";

onLaunch(() => {
	// #ifdef H5
	// if (!window.isInitScriptExecuted) {
	//   window.isInitScriptExecuted = true;

	// }else{
	//   return;
	// }
	// #endif
	console.log("onLaunch!")
	init()
})

</script>
<style>
@import url('styles/tailwind.css');

.status_bar {
	height: var(--status-bar-height);
	width: 100%;
}
</style>
