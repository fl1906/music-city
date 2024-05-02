<template>
	<view class="update">
		<NavBar :isBack="false" title="修改信息" />
		<AcTime @getTime="getTime" :time="time">
			<Card title="活动地址" :extra="true">
				<view class="info ac-address u-flex" @click="selectAddress">
					<image class="pi" src="../../../../static/get-position.png"></image>
					<text class="desc">{{address}}</text>
					<image src="../../../../static/arrow.png" class="right"></image>
				</view>
			</Card>
		</AcTime>
		<button class="submit" @click="submit">保存</button>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<view>对方想要超限制确认</view>
			<view>已与本人见面</view>
		</Popup>
	</view>
</template>

<script>
	import AcTime from "@/components/ac-time/ac-time.vue"
	import NavBar from "@/components/navbar/navbar.vue"
	import Card from "@/components/card/card.vue"
	import Api from '@/api/index.js'
	import dayjs from "../../../../common/dayjs/day.js"
	export default {
		components: {
			AcTime,
			NavBar,
			Card
		},
		data() {
			return {
				time: "",
				address: "",
				group: null
			}
		},
		methods: {
			submit() {
				const {
					time,
					address,
					group
				} = this;
				Api.putActivityGroup({
					...group,
					activityTime: time,
					address
				}, (res) => {
					uni.navigateBack({
						delta: 1
					})
				})
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
								const address = res.data.result.address
								_this.address = address
							}
						})
					}
				});
			},
			getTime(val) {
				this.time = val
			},
			getApplyUserInfo(groupId, userId) {
				Api.getApplyUserInfo({
					groupId,
					userId,
				}, (res) => {
					const data = res.data.data
					this.group = data.pzcActivityGroup;
					this.address = data.pzcActivityGroup.address
					this.time = data.pzcActivityGroup.activityTime
				})
			},
		},
		onLoad(l) {
			const {
				userId,
				groupId
			} = l;
			this.getApplyUserInfo(groupId, userId)
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>