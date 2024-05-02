<template>
	<view>
		<view class="my">
			<NavBar :isBack="false" title="我的" />
			<!-- 头部背景 -->
			<view class="head-bg"></view>
			<!-- 登录信息 -->
			<view class="login-info flex">
				<view class="avatar flex">
					<image src="../../static/avatar.png" v-if="!userInfo"></image>
					<image :src="userInfo.avatar" v-else></image>
					<button class="info flex button" v-if="!userInfo" plain open-type="getPhoneNumber"
						@getphonenumber="onGetPhoneNumber">
						<view class="title">登录</view>
						<view class="desc">登录享受更多精彩信息</view>
					</button>
					<view class="info flex" v-else>
						<view class="title">{{userInfo.nickname}}</view>
						<view class="desc">{{userInfo.address ?userInfo.address:"未知地区"}}</view>
					</view>
				</view>
				<button class="btn" v-if="!userInfo" open-type="getPhoneNumber" @getphonenumber="onGetPhoneNumber">
					点击登录
				</button>
				<button class="btn" @click="toPage('edit')" v-else>
					<image src="../../static/edit-icon.png" class="edit-icon"></image>
					编辑资料
				</button>
			</view>
			<!-- 等级信息 -->
			<view class="grade flex">
				<image src="../../static/grade-bg.png" class="grade-bg"></image>
				<view class="grade-info">
					<view class="level-info flex">
						<image src="../../static/grade-icon.png" class="grade-icon"></image>
						<view class="level">Lv.{{userInfo.userLevel?userInfo.userLevel:0}}</view>
					</view>
					当前积分：{{userInfo.integrationNow?userInfo.integrationNow:0}}分
				</view>
				<button @click="toPage('level')">等级中心</button>
			</view>
			<view class="bottom">
				<image src="../../static/my-bg.png" class="my-bg"></image>
				<view class="bottom-top flex">
					<!-- 账号信息 -->
					<view class="about">
						<view class="pai">
							<image src="../../static/paibi-icon.png" class="paibi-icon"></image>
						</view>
						<view class="my-pai" @click="toPage('paibi')">
							<view>我的派币</view>
							<view class="flex">
								<image src="../../static/paibi.png" class="paibi"></image>
								x
								<view style="font-size: 24rpx;">{{userInfo.money}}</view>
							</view>
						</view>
					</view>
					<!-- 操作按钮 -->
					<view class="handle">
						<view class="nav flex" @click="toPage('nav')">
							<image src="../../static/nav-icon.png" class="nav-icon"></image>
							我的收藏
						</view>
						<view class="history-active flex">
							<image src="../../static/history-active.png" class="history-active-img"></image>
							历史活动
						</view>
					</view>
				</view>
				<!-- 卡片设置bar -->
				<view class="card-bar">
					<view class="card-title">其他服务</view>
					<u-cell-item title="设置" :border-bottom="false" hover-class="none">
						<image slot="icon" src="../../static/setting-icon.png" class="icon"></image>
					</u-cell-item>
					<u-cell-item title="帮助中心" :border-bottom="false" hover-class="none">
						<image slot="icon" src="../../static/help-icon.png" class="icon"></image>
					</u-cell-item>
					<u-cell-item title="浏览历史" :border-bottom="false" hover-class="none">
						<image slot="icon" src="../../static/hisotry-icon.png" class="icon"></image>
					</u-cell-item>
					<u-cell-item title="我的订单" :border-bottom="false" hover-class="none">
						<image slot="icon" src="../../static/order-icon.png" class="icon-order"></image>
					</u-cell-item>
				</view>
			</view>
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
		<Tabbar activeKey="my" />
	</view>
</template>

<script>
	import Tabbar from "@/components/tabbar/tabbar.vue"
	import NavBar from "@/components/navbar/navbar.vue"
	import Api from "@/api/index.js"
	import {
		mapActions,
		mapState
	} from 'vuex'

	export default {
		components: {
			NavBar,
			Tabbar
		},
		computed: {
			...mapState(['userInfo'])
		},
		data() {
			return {}
		},
		onLoad() {},
		methods: {
			...mapActions([
				'setUserInfo',
				'setToken',
				'getUserInfo'
			]),
			// 跳转页
			toPage(path) {
				uni.navigateTo({
					url: `/pages/my/${path}/index`
				})
			},
			// 获取手机号
			onGetPhoneNumber(e) {
				if (e.detail.errMsg == "getPhoneNumber:ok") {
					const phoneCode = e.detail.code
					let that = this;
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							const {
								errMsg,
								code
							} = loginRes
							if (code) {
								uni.getUserInfo({
									provider: 'weixin',
									success: function(infoRes) {
										const {
											userInfo
										} = infoRes

										const data = {
											loginCode: code,
											phoneCode,
											nickname: userInfo.nickName,
											avatar: userInfo.avatarUrl,
											sex: userInfo.gender
										}

										Api.wxLogin(data, (res) => {
											const token = res.data.msg
											uni.setStorage({
												key: 'token',
												data: token
											})
											that.setToken(token)
											// 登录成功后，请求个人信息
											Api.getUserInfo((res) => {
												const userInfo = res.data.data
												uni.setStorage({
													key: 'userInfo',
													data: userInfo
												})
												that.setUserInfo(userInfo)
											})
										})
									}
								});
							} else {
								console.log('登录失败！' + res.errMsg)
							}
						}
					});
				} else {
					console.log("用户点击了拒绝")
				}
			},
		},
		onLoad() {
			if (this.userInfo) {
				this.getUserInfo()
			}
		}
	}
</script>

<style>
	@import url("index.css");
</style>