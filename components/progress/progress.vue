<template>
	<view class="progress">
		<view class="line">
		</view>
		<view class="progress-active u-flex">
			<view class="line" :style="{width:progress+'%'}" />
			<text>完整度{{progress}}%</text>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			progress: Number
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>