<template>
	<view class="tabbar">
		<image src="../../static/tabbar-bg.png" class="tabbar-img"></image>
		<view class="tabbar-items">
			<view class="tabbar-item" @click="navigate('index')">
				<image src="../../static/index-icon.png" class="icon" v-if="activeKey!=='index'"></image>
				<image src="../../static/index-icon-selected.png" class="icon" v-else></image>
				<view :class="{ active:activeKey ==='index','tabbar-item-text':true }">首页</view>
			</view>
			<view class="tabbar-item" @click="navigate('trip')">
				<image src="../../static/mid-button.png" class="mid-icon"></image>
			</view>
			<view class="tabbar-item" @click="navigate('my')">
				<image src="../../static/my-icon.png" class="icon" v-if="activeKey!=='my'"></image>
				<image src="../../static/my-icon-selected.png" class="icon" v-else></image>
				<view :class="{ active:activeKey ==='my','tabbar-item-text':true }">我的</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "tabbar",
		props: {
			activeKey: String,
		},
		data() {
			return {

			};
		},
		methods: {
			navigate(path) {
				if (path === this.activeKey) return
				if (path === 'trip') {
					uni.navigateTo({
						url: `/pages/trip/index`
					})
					return
				}
				uni.redirectTo({
					url: `/pages/${path}/index`
				})
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>