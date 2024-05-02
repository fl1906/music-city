<template>
	<scroll-view :scroll-y="true" :style="{height}" @scrolltolower="scrollBottom">
		<u-loading :show="loading"></u-loading>
		<slot v-if="!loading"></slot>
	</scroll-view>
</template>

<script>
	export default {
		props: {
			height: String,
			loading: Boolean
		},
		methods: {
			scrollBottom() {
				this.$emit('scrollBottom')
			}
		}
	}
</script>

<style scoped>
	@import url("index.css");
</style>