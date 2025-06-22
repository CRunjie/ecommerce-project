<template>
 <HeaderCom />
 <SidebarCom />
	<div class="content-box">
			<div class="content">
				<router-view></router-view>
			</div>
		</div>
</template>

<script setup>
import HeaderCom from "../components/HeaderCom.vue"
import SidebarCom from "../components/SidebarCom.vue"
</script>
<style scoped>
	.content-box {
		position: absolute;
		left: 250px;
		right: 0;
		top: 70px;
		bottom: 0;
		padding-bottom: 30px;
		-webkit-transition: left 0.3s ease-in-out;
		transition: left 0.3s ease-in-out;
		background: #f0f0f0;
	}
	.content-collapse {
		left: 65px;
	}
	.content {
		width: auto;
		height: 99%;
		padding: 10px;
		box-sizing: border-box;
		background: #efefef;
		overflow-y: auto;
	}
</style>
