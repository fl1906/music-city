<template>
	<view class="mate">
		<NavBar :isBack="false" title="匹配" />
		<map class="map" id="mapS" :latitude="latitude" :longitude="longitude" :scale="17" :markers="markers"
			:polyline="polyline" ref="mapS">
		</map>
		<cover-view class="current-position u-flex" v-if="btnType!==3 || btnType !==-1">
			<cover-view class="text">当前位置：{{currentAddress}}</cover-view>
			<cover-image class="update" src="../../../static/refresh.png"
				@click="getCheckInAddress(address)"></cover-image>
		</cover-view>
		<cover-view class="cover">
			<cover-view class="card">
				<cover-image class="card-bg" src="../../../static/be-confirm-bg.png"
					v-if="role==1 && (btnType == 0)"></cover-image>
				<cover-image class="card-bg" src="../../../static/daidaoda.png"
					v-if="btnType ==11 || btnType ==12 || btnType ==2"></cover-image>
				<cover-image class="card-bg" src="../../../static/daiqueren.png"
					v-if="(btnType==0 && role==0) || btnType == 1 || btnType ==9 || btnType == 10"></cover-image>
				<cover-image class="card-bg" src="../../../static/daipingjia.png" v-if="btnType==3"></cover-image>
				<cover-view class="avatars u-flex">
					<cover-image :src="teamUp.avatar" class="avatar first">
					</cover-image>
					<cover-image src="../../../static/link-icon-yellow.png" class="link-icon-yellow"></cover-image>
					<cover-image :src="userInfo.avatar" class="avatar second"></cover-image>
				</cover-view>
				<cover-view class="bar">
					<cover-view class="title u-flex">
						{{group.title}}
					</cover-view>
					<cover-view class="u-flex sub-info">
						<cover-view class="level">{{teamUp.userLevel}}级</cover-view>
						<cover-image src="../../../static/split-line.png" class="split-line"></cover-image>
						<cover-view class="pay-type">{{returnGroupTypeLabel(group.groupType)}}</cover-view>
						<cover-image src="../../../static/split-line.png" class="split-line"></cover-image>
						<cover-view class="paibi u-flex">
							<cover-image src="../../../static/paibi.png" class="paibi-logo"></cover-image>
							<cover-view class="tag">X</cover-view>
							<cover-view class="count">{{group.money}}</cover-view>
						</cover-view>
					</cover-view>
					<button class="cancel" @click="whenShow = true"
						v-if="btnType==2 || btnType ==11 || btnType==12">取消组队</button>
					<cover-view class="handle u-flex">
						<cover-view class="left u-flex">
							<cover-image :src="teamUp.avatar" class="avatar"></cover-image>
							<cover-view class="status"
								:style="{background:liveStatus?'#49C265':'#F55433'}"></cover-view>
							<cover-view class="info">
								<cover-view class="title u-flex">{{teamUp.nickname}}</cover-view>
								<cover-view class="desc u-flex">
									<cover-image class="msg-icon"
										src="../../../static/msg-icon-yellow.png"></cover-image>
									<cover-view>{{teamUp.intro?teamUp.intro:"-"}}</cover-view>
								</cover-view>
							</cover-view>
						</cover-view>
						<cover-view class="right u-flex">
							<button class="count" v-if="teamUp.notReadCount !=0">{{teamUp.notReadCount}}</button>
							<cover-image class="msg" src="../../../static/msg-icon.png" @click="toChat"></cover-image>
							<cover-image class="call" src="../../../static/phone-icon.png"
								@click="callPhone(teamUp.phone)"></cover-image>
						</cover-view>
					</cover-view>
				</cover-view>
			</cover-view>
			<cover-view class="position" v-if="!btnType==0">
				<cover-view class="time u-flex">
					<cover-image src="../../../static/time-icon-map.png" class="time-icon"></cover-image>
					<cover-view class="activity-time">{{group.activityTime}}</cover-view>
				</cover-view>
				<cover-view class="local u-flex">
					<cover-image src="../../../static/address-icon.png" class="address-icon"></cover-image>
					<cover-view>{{group.address}}</cover-view>
				</cover-view>
			</cover-view>
			<cover-view class="confirmed" v-if="(btnType==9 && role==1)||(btnType==10 && role==0)">等待对方确认</cover-view>
			<cover-view class="confirmed" v-if="btnType ==-1">已取消</cover-view>
			<cover-view class="ac-time" @click="toUpdate" v-if="role===1 && (btnType == 0|| btnType ==1)">
				<cover-view class="title u-flex">
					<cover-image src="../../../static/line-yellow.png" class="line-yellow"></cover-image>
					<cover-view>活动时间</cover-view>
				</cover-view>
				<cover-view class="info">
					<cover-view class="date u-flex">
						<cover-view class="input">
							{{year}}
						</cover-view>
						<cover-view>年</cover-view>
						<cover-view class="input">
							{{month}}
						</cover-view>
						<cover-view>月</cover-view>
						<cover-view class="input">
							{{day}}
						</cover-view>
						<cover-view>日</cover-view>
					</cover-view>
					<cover-view class="select-time u-flex">
						<cover-view class="input">
							{{currentTime}}
						</cover-view>
						<cover-view class="pm-am">{{amOrPm}}</cover-view>
					</cover-view>
				</cover-view>
				<cover-view class="title u-flex">
					<cover-image src="../../../static/line-yellow.png" class="line-yellow"></cover-image>
					<cover-view>活动地址</cover-view>
				</cover-view>
				<cover-view class="ac-address u-flex">
					<cover-image class="pi" src="../../../static/get-position.png"></cover-image>
					<cover-view class="desc">{{address}}</cover-view>
					<cover-image src="../../../static/arrow.png" class="right"></cover-image>
				</cover-view>
			</cover-view>
			<!-- 当前用户没确认 -->
			<cover-view class="btns u-flex"
				v-if="((btnType==0 && role===1) || btnType==1) || ((btnType==9 && role==0)||(btnType==10 && role==1))">
				<cover-view class="cancel" @click="show = true">
					<cover-view class="tag">取消</cover-view>
					<cover-view>（1次免责取消）</cover-view>
				</cover-view>
				<button class="submit" @click="comfirmTeamUp(applyId)">确认</button>
			</cover-view>
			<!-- 待到达 -->
			<cover-view class="btns" v-if="btnType == 2 || btnType == 11 || btnType ==12">
				<button class="confirm" @click="confirmReach(applyId)">确认到达目的地</button>
				<button class="more-time" @click="toMoreTime">超限制确认到达</button>
			</cover-view>
			<cover-view class="confirmed"
				v-if="(btnType ==11 && role==1) || (btnType ==12 && role==0)">等待对方到达该位置</cover-view>
			<button class="evaluate" v-if="btnType ==3">立即评价</button>
		</cover-view>
		<cover-image src="../../../static/position-icon-map.png" class="origin"></cover-image>
		<Popup :show="whenShow" mode="whenShow" @close="whenShow =false" @confirm="cancelTeamUp(applyId)">
		</Popup>
		<Popup :show="show" mode="cancel" @close="show =false" @confirm="cancelTeamUp(applyId)">
		</Popup>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Api from "@/api/index.js"
	import utils from "@/utils/index.js"
	import AcTime from "@/components/ac-time/ac-time.vue"
	import dayjs from "../../../common/dayjs/day.js"
	import Popup from "@/components/popup/popup.vue"
	import config from "@/common/lib/config.js"
	import {
		mapState,
		mapActions
	} from "vuex"
	export default {
		components: {
			NavBar,
			AcTime,
			Popup
		},
		watch: {
			address(val) {
				const _this = this;
				this.getLalo(val, ({
					lat,
					lng
				}) => {
					_this.markers.push({
						id: 112,
						latitude: lat,
						longitude: lng,
						iconPath: '../../../static/position-blue.png',
						width: '47rpx',
						height: '67rpx',
						label: {
							content: `目的地`,
							fontSize: 10,
							color: "#FEFEFF",
							borderRadius: 30,
							bgColor: '#19102B40',
							padding: 8,
							anchorX: -25,
							anchorY: 10
						}
					})
				})
			}
		},
		computed: {
			...mapState(['userInfo']),
		},
		data() {
			return {
				whenShow: false,
				liveStatus: false,
				timer: null,
				show: false,
				role: -1, // 0 是申请方 1 是发起方
				teamUp: null,
				group: null,
				applyId: null,
				year: "",
				month: "",
				day: "",
				currentTime: "",
				address: "",
				currentAddress: "",
				amOrPm: "AM",
				btnType: -2, // -1 已取消 0 位于申请列表中 1 申请通过待确认时 2 确认通过进行中 3 组队结束 9发起方已确认 10申请方已确认 11 发起方已打卡 12 申请方己打卡
				latitude: 39.909, // 默认纬度
				longitude: 116.39742, // 默认经度(北京天安门)
				markers: [{
					id: 110,
					latitude: 31.99226,
					longitude: 118.7787,
					iconPath: '../../../static/position-blue.png',
					width: '47rpx',
					height: '67rpx',
					label: {
						content: `我在这里`,
						fontSize: 10,
						color: "#FEFEFF",
						borderRadius: 30,
						bgColor: '#19102B40',
						padding: 8,
						anchorX: -25,
						anchorY: 10
					}
				}],
				polyline: [],
				canReach: false
			}
		},
		methods: {
			...mapActions(['sendMoreTime']),
			// 确认到达
			confirmReach(applyId) {
				if (!this.canReach) {
					uni.showToast({
						title: "当前位置未抵达打卡点",
						icon: "error"
					})
					return
				}
				let _this = this;
				Api.confirmReach(applyId, (res) => {
					_this.getApplyUserInfo(_this.groupId, _this.userId)
				})
			},
			// 组队确定
			comfirmTeamUp(applyId) {
				Api.confirmTeamUp(applyId, (res) => {
					console.log(res)
				})
			},
			// 地址转经纬度
			getLalo(address, success) {
				const Key = config.txKey
				uni.request({
					url: `https://apis.map.qq.com/ws/geocoder/v1/?address=`,
					data: {
						key: Key,
						address
					},
					success: (res) => {
						const location = res.data.result.location
						success(location)
					}
				})
			},
			// 获取离打卡点的位置
			getCheckInAddress(keyword) {
				uni.showLoading()
				const Key = config.txKey
				const _this = this;
				const {
					applyId,
					role,
				} = this;

				uni.getLocation({
					altitude: true,
					isHighAccuracy: true,
					success: function(res) {
						const latitude = res.latitude
						const longitude = res.longitude;
						// 更新当前的位置坐标
						_this.getCurrentAddress(longitude, latitude, () => {
							// 向服务端提交自己的地址
							Api.postRefurbish({
								applyId,
								role,
								address: _this.currentAddress
							}, (res) => {
								const {
									applyAddress,
									startAddress
								} = res.data.data
								const address = role == 0 ? startAddress : applyAddress
								_this.getLalo(address, ({
									lat,
									lng
								}) => {
									_this.drawPostion(lng, lat)
								})
							})
						})
						// 更新定位图片的位置坐标
						_this.markers[0].longitude = longitude
						_this.markers[0].latitude = latitude
						// 500米内可打卡
						uni.request({
							url: `https://apis.map.qq.com/ws/place/v1/search`,
							data: {
								key: Key,
								boundary: `nearby(${latitude},${longitude},500,0)`,
								keyword,
								page_size: 1,
								page_index: 1,
							},
							success: (res) => {
								const data = res.data.data
								if (data.length !== 0) {
									_this.canReach = true
									// 500米内可以搜到地点，设置打卡marker
									_this.markers[0].label = {
										content: `当前位置可打卡`,
										fontSize: 10,
										color: "#272233",
										borderRadius: 30,
										bgColor: '#FFF536',
										padding: 8,
										anchorX: -40,
										anchorY: -75
									}
								}
							}
						})
						uni.hideLoading()
					},
					fail(err) {
						console.log(err)
					}
				});
			},
			getCurrentAddress(longitude, latitude, success) {
				const _this = this;
				const Key = config.txKey
				uni.request({
					url: `https://apis.map.qq.com/ws/geocoder/v1/`,
					data: {
						key: Key,
						location: `${latitude},${longitude}`
					},
					success: (res) => {
						const address = res.data.result.address
						_this.currentAddress = address
						success(address)
					}
				})
			},
			toMoreTime() {
				this.sendMoreTime({
					fromUserId: this.userInfo.userId,
					toUserId: this.userId,
					applyId: this.applyId
				})
				uni.showToast({
					title: '发送成功',
					icon: 'success'
				})
			},
			toUpdate() {
				uni.navigateTo({
					url: `/pages/trip/mate/update/index?groupId=${this.groupId}&userId=${this.userId}`
				})
			},
			setTime(v) {
				console.log(v)
				this.time = v
			},
			// 前往聊天
			toChat() {
				Api.PostTalkRead(this.userId)
				uni.navigateTo({
					url: `/pages/chat/msg/index?toUserId=${this.userId}`
				})
			},
			callPhone(phone) {
				uni.makePhoneCall({
					phoneNumber: phone
				});
			},
			// 获取申请方还是发起方
			getApplyRole(applyId, success) {
				Api.getApplyRole(applyId, (res) => {
					const data = res.data.data
					this.role = data
					success(data)
				})
			},
			returnGroupTypeLabel(val) {
				return utils.returnGroupTypeLabel(val)
			},
			// 获取组队个人信息
			getApplyUserInfo(groupId, userId) {
				Api.getApplyUserInfo({
					groupId,
					userId
				}, (res) => {
					const data = res.data.data
					this.teamUp = data
					this.group = data.pzcActivityGroup

					const status = data.pzcActivityGroupApplyVo.applyStatus
					this.btnType = status

					this.address = data.pzcActivityGroup.address
					// 在线状态
					this.liveStatus = data.liveStatus
					const time = data.pzcActivityGroup.activityTime
					const newDayjs = dayjs(time)
					this.year = newDayjs.year()
					this.month = newDayjs.month() + 1
					this.day = newDayjs.date()
					this.hour = newDayjs.hour()
					this.minute = newDayjs.minute()
					this.confirm({
						hour: newDayjs.hour(),
						minute: newDayjs.minute() == 0 ? newDayjs.minute() + "0" : newDayjs.minute()
					})
				})
			},
			confirm(value) {
				const {
					hour,
					minute
				} = value
				this.currentTime = `${hour}:${minute}`
				this.amOrPm = hour > 12 ? 'PM' : 'AM'
			},
			// 设置位置
			handlePostion(applyId) {
				let _this = this;
				uni.getLocation({
					altitude: true,
					isHighAccuracy: true,
					success: function(res) {
						const latitude = res.latitude
						const longitude = res.longitude;
						_this.latitude = latitude;
						_this.longitude = longitude;
						_this.markers[0].latitude = latitude
						_this.markers[0].longitude = longitude
						_this.getCurrentAddress(longitude, latitude, (address) => {
							_this.getApplyRole(applyId, (role) => {
								// 发送自己的位置
								Api.postRefurbish({
									applyId,
									role,
									address
								})
							})
						})
					},
					fail(err) {
						console.log(err)
					}
				});
				let map = uni.createMapContext('mapS');
				map.setCenterOffset({
					offset: [0.5, 0.35]
				})
			},
			cancelTeamUp(applyId) {
				Api.cancelTeamUp(applyId, (res) => {
					this.show = false
				})
			},
			// 描绘两点的距离
			drawPostion(longitude, latitude) {
				let _this = this;
				const Key = config.txKey
				//通过wx.request发起HTTPS接口请求
				uni.request({
					url: `https://apis.map.qq.com/ws/direction/v1/walking/?key=${Key}&from=${_this.latitude},${_this.longitude}&to=${latitude},${longitude}`,
					success(res) {
						console.log(res.data)
						var result = res.data.result
						var route = result.routes[0]
						// 终点的marker
						const distance = (route.distance / 1000).toFixed(1)
						_this.markers.push({
							id: 111,
							latitude,
							longitude,
							iconPath: _this.teamUp.avatar,
							width: '40',
							height: '40',
							label: {
								content: `距您${distance}公里`,
								fontSize: 10,
								color: "#FEFEFF",
								borderRadius: 30,
								bgColor: '#19102B40',
								padding: 8,
								anchorX: -25,
								anchorY: 10
							}
						})
						var coors = route.polyline,
							pl = [];
						//坐标解压（返回的点串坐标，通过前向差分进行压缩）
						var kr = 1000000;
						for (var i = 2; i < coors.length; i++) {
							coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
						}
						//将解压后的坐标放入点串数组pl中
						for (var i = 0; i < coors.length; i += 2) {
							pl.push({
								latitude: coors[i],
								longitude: coors[i + 1]
							})
						}
						// 将路线的起点设置为地图中心点
						_this.latitude = pl[0].latitude
						_this.longitude = pl[0].longitude
						// 绘制路线
						_this.polyline.push({
							points: pl,
							color: '#FFF536',
							width: 6,
							borderColor: '#FFF536',
							borderWidth: 1
						})
					}
				})
			}
		},
		onLoad(l) {
			const {
				activityId,
				groupId,
				applyId,
				userId,
			} = l;
			this.userId = userId
			this.activityId = activityId
			this.groupId = groupId
			this.applyId = applyId
			this.getApplyUserInfo(groupId, userId)
			this.handlePostion(applyId)
			const timer = setInterval(() => {
				this.getApplyUserInfo(groupId, userId)
			}, 5000)
			this.timer = timer
		},
		onShow() {
			const {
				groupId,
				userId
			} = this;
			if (groupId && userId) {
				this.getApplyUserInfo(groupId, userId)
			}
		},
		onUnload() {
			this.timer = null
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>