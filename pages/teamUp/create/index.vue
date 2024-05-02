<template>
	<view class="create">
		<NavBar :isBack="false" title="创建组队" />
		<Card title="基本信息" class="basic">
			<image :src="userInfo.avatar" class="avatar"></image>
			<view class="info u-flex">
				<view class="item u-flex">
					<image src="../../../static/level-icon-yellow.png" class="level-icon-yellow"></image>
					<view class="level">等级</view>
					<view class="level-type">{{userInfo.userLevel}}级</view>
				</view>
				<view class="item u-flex">
					<image src="../../../static/gender-icon-yellow.png" class="gender-icon-yellow"></image>
					<view class="level">性别</view>
					<view class="level-type">{{userInfo.sex ===1?'男':'女'}}</view>
				</view>
				<view class="item u-flex">
					<image src="../../../static/ac-age-icon.png" class="ac-age-icon"></image>
					<view class="level">年龄</view>
					<view class="level-type">{{userInfo.age}}岁</view>
				</view>
				<view class="item region u-flex">
					<image src="../../../static/region-icon-yellow.png" class="region-icon-yellow"></image>
					<view class="level">地区</view>
					<view class="level-type">{{userInfo.address}}</view>
				</view>
			</view>
		</Card>
		<Card title="活动信息" class="ac">
			<view class="ac-info">
				<AcTitle @getTitle="setTitle" />
				<FundAndbudget @getFund="setFund" @getBudget="setBudget" />
			</view>
		</Card>
		<AcTime class="ac-time" @getTime="setTime">
			<Card title="活动地址" :extra="true">
				<view class="info ac-address u-flex" @click="selectAddress">
					<image class="pi" src="../../../static/get-position.png"></image>
					<text class="desc">{{address}}</text>
					<image src="../../../static/arrow.png" class="right"></image>
				</view>
			</Card>
			<Card title="权限设置" :extra="true">
				<view class="info ac-auth u-flex" @click="setShowAuth">
					{{currentAuth?currentAuth.label:""}}
					<image src="../../../static/xiala.png" class="right"></image>
				</view>
				<u-select v-model="showAuth" :list="listAuth" @confirm="confirmAuth"></u-select>
			</Card>
		</AcTime>
		<view class="submit u-flex" @click="submit">确认创建</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Card from "@/components/card/card.vue"
	import FundAndbudget from "@/components/fundAndbudget/fundAndbudget.vue"
	import AcTitle from "@/components/ac-title/ac-title.vue"
	import AcTime from "@/components/ac-time/ac-time.vue"
	import Api from "@/api/index.js"
	import {
		mapState
	} from 'vuex'
	export default {
		components: {
			NavBar,
			Card,
			FundAndbudget,
			AcTitle,
			AcTime
		},
		computed: {
			...mapState(['userInfo'])
		},
		data() {
			return {
				title: "",
				fund: "",
				year: "",
				month: '',
				day: '',
				time: '',
				budget: '',
				currentAuth: null,
				address: '点击选择活动地址',
				showAuth: false,
				listAuth: [{ // 0 所有人可见 1 可见1张照片 2 不可见所有信息
						value: 0,
						label: '所有人可见'
					},
					{
						value: 1,
						label: '可见1张照片'
					}, {
						value: 2,
						label: '不可见所有信息'
					}
				],
			}
		},
		methods: {
			submit() {
				const {
					title,
					fund,
					time,
					currentAuth,
					budget,
					address,
					activityId
				} = this;
				const data = {
					activityId,
					title,
					money: fund,
					activityTime: time,
					auth: currentAuth.value,
					address,
					groupType: budget.value
				}
				Api.postActivityGroup(data, (res) => {
					uni.showToast({
						title: "创建成功",
						icon: 'success'
					})
					uni.navigateBack({
						delta: 1
					})
				})
			},
			setTime(v) {
				this.time = v
			},
			setFund(v) {
				this.fund = v
			},
			setTitle(v) {
				this.title = v
			},
			setBudget(v) {
				this.budget = v
			},
			setShowAuth() {
				this.showAuth = !this.showAuth
			},
			confirmAuth(value) {
				this.currentAuth = value[0]
			},
			selectAddress() {
				const Key = "UU6BZ-44QWT-XCMXL-V2M6A-DYU6V-B3FRF"
				let _this = this;
				uni.chooseLocation({
					success: function(res) {
						const longitude = res.longitude;
						const latitude = res.latitude;
						uni.request({
							url: `https://apis.map.qq.com/ws/geocoder/v1/`,
							data: {
								key: Key,
								location: `${latitude},${longitude}`
							},
							success: (res) => {
								const address = res.data.result.formatted_addresses.recommend
								_this.address = address
							}
						})
					}
				});
			}
		},
		onLoad(l) {
			const {
				activityId
			} = l
			this.activityId = activityId
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>