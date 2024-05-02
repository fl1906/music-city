<template>
	<view class="dropdown">
		<view class="mask" v-if="open" @click="selectOpen" :style="{height:(1500-height) + 'rpx'}"></view>
		<view :class="{title:true,'u-flex':true,active:open}" :style="{fontSize:titleSize +'rpx'}" @click="selectOpen">
			{{title}}
			<image src="../../static/shangla.png" v-if="open"></image>
			<image src="../../static/xiala.png" v-else></image>
		</view>
		<view class="select-bar" :style="{height:height+'rpx'}" v-if="open">
			<slot></slot>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			titleSize: String,
			title: String,
			height: {
				type: String,
				default: '716'
			}
		},
		data() {
			return {
				open: false
			}
		},
		methods: {
			selectOpen() {
				this.open = !this.open
			},
			closeOpen(){
				this.open = false
			}
		},
	}
</script>

<style scoped>
	@import url("index.css");
</style>