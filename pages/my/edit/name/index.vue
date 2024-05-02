<template>
	<view class="name">
		<NavBar :isBack="false" title="修改昵称" />
		<view class="tip">七天内可修改一次名字</view>
		<view class="value">
			<input type="text" maxlength="24" v-model="value">
			{{value.length}}/24
		</view>
		<view class="limit">请设置2-24个字符，不包括&gt;/等无效字符哦</view>
		<button class="submit" @click="submit">保存</button>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
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
				value: ""
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			submit() {
				let _this = this;
				Api.putUserInfo({
					nickname: _this.value
				}, () => {
					_this.getUserInfo();
					uni.navigateBack({
						delta: 1
					})
				})
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>