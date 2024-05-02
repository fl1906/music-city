<template>
	<view class="ac-title">
		<view class="u-flex">
			<image src="../../static/ac-title-icon.png"></image>
			活动主题
		</view>
		<input v-model="title" />
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: ""
			}
		},
		watch: {
			title(val) {
				this.$emit('getTitle', val)
			}
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>