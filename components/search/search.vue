<template>
	<view class="search">
		<image src="../../static/search-bg.png" class="search-bg"></image>
		<image src="../../static/search-icon.png" class="search-icon"></image>
		<input v-model="data" class="search-input" :placeholder="placeholder" />
	</view>
</template>

<script>
	export default {
		props: {
			placeholder: String,
		},
		watch: {
			data(val) {
				this.$emit('change', val)
			}
		},
		data() {
			return {
				data: ""
			}
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>