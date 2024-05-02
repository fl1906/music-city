<template>
	<view class="box">
		<NavBar :isBack="false" title="个人介绍" />
		<view class="value">
			<input type="text" v-model="value">
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
		<button class="submit" @click="submit">保存</button>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import {
		mapState,
		mapActions
	} from "vuex"
	import Api from '@/api/index.js'

	export default {
		components: {
			NavBar,
		},
		data() {
			return {
				value: "",
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			submit() {
				let _this = this;
				if (!this.value) {
					uni.showToast({
						title: '请填写所有项',
						icon: 'error',
					})
					return
				}
				Api.putUserInfo({
					intro: this.value,
				}, () => {
					_this.getUserInfo();
					uni.navigateBack({
						delta: 1
					})
				})
			},
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>