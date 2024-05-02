<template>
	<view class="paibi">
		<NavBar :isBack="false" title="派币" />
		<view class="top u-flex">
			<view class="left">
				<view class="title">我的派币</view>
				<view class="count">{{userInfo.money}}</view>
			</view>
			<image src="../../../static/paibi-logo.png" class="paibi-logo"></image>
		</view>
		<view class="bottom">
			<Tabs :current="current" :list="list" @change="changeTab" />
			<template v-if="current==0">
				<view class="title">充值</view>
				<view class="charge u-flex">
					<view class="charge-item u-flex" v-for="(item,index) in charges" :key="index"
						@click="select(item.value)">
						<image src="../../../static/paibi.png"></image>
						<view>{{item.label}}</view>
						<view>￥{{realRmb(item.value)}}</view>
					</view>
					<view class="charge-item u-flex" @click="showPopup">
						<image src="../../../static/paibi.png"></image>
						<view>自定义</view>
						<view>￥10</view>
					</view>
					<u-popup v-model="show" mode="center">
						<view class="pop">
							<view class="pop-title">自定义金额</view>
							<input v-model="customValue" placeholder="最低0.1个派币(￥10)" />
							<view class="btns u-flex">
								<button @click="showPopup">取消</button>
								<button type="primary" @click="submit">提交</button>
							</view>
						</view>
					</u-popup>
				</view>
				<view class="pay u-flex">
					<view class="wx u-flex">
						<image src="../../../static/wxpay.png" class="wxpay"></image>
						<text>微信支付</text>
					</view>
					<image src="../../../static/gou.png" class="gou"></image>
				</view>
				<button class="submit u-flex" @click="getPayParams">确定支付{{realRmb(count)}}元</button>
			</template>
			<template v-else>
				<view class="bill">
					<view class="info u-flex">
						<view class="time u-flex" @click="showSelectTime=true">
							{{selectTime?`${selectTime.year}年${selectTime.month}月`:"账单日期"}}
							<image src="../../../static/xiala.png" v-if="!showSelectTime"></image>
							<image src="../../../static/shangla.png" v-else></image>
						</view>
						<u-picker v-model="showSelectTime" mode="time" :params="params"
							@confirm="confirmSelectTime"></u-picker>
					</view>
					<ScrollList class="bill-items" @scrollBottom="scrollBottom" height="50vh">
						<view v-for="(item,index) in payList" :key="index">
							<view class="item u-flex">
								<view class="item-left u-flex">
									<image src="../../../static/paibi.png"></image>
									<view class="item-left-info u-flex">
										<view class="item-title">派币-{{item.type==4?"支出":"收入"}}</view>
										<view class="reazon">{{item.message}}</view>
									</view>
								</view>
								<view class="item-right u-flex">
									<view :class="{'pay-count':true,active:item.type==2}">
										{{item.money}}
									</view>
									<view class="pay-time">{{getTimeFormat(item.createTime)}}</view>
								</view>
							</view>
							<view class="line" v-if="index !== payList.length -1"></view>
						</view>
					</ScrollList>
				</view>
			</template>
		</view>
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
	import {
		mapState,
		mapActions
	} from "vuex"
	import dayjs from '../../../common/dayjs/day.js'
	import ScrollList from "@/components/scroll-list/scroll-list.vue"
	export default {
		components: {
			NavBar,
			ScrollList
		},
		data() {
			return {
				params: {
					year: true,
					month: true,
					day: false,
					hour: false,
					minute: false,
					second: false
				},
				selectTime: null,
				customValue: null,
				show: false,
				showSelectTime: false,
				charges: [{
						label: '0.1派币',
						value: this.realPaibi(10)
					},
					{
						label: '0.2派币',
						value: this.realPaibi(20)
					},
					{
						label: '0.5派币',
						value: this.realPaibi(50)
					},
					{
						label: '1派币',
						value: this.realPaibi(100)
					},
					{
						label: '2派币',
						value: this.realPaibi(200)
					},
				],
				count: 0,
				current: 1,
				page: 1,
				pageNum: 10,
				list: [{
					name: '派币充值'
				}, {
					name: '账单'
				}],
				payList: []
			}
		},
		watch: {
			page() {
				this.getHisotyPayList()
			},
			selectTime() {
				this.getHisotyPayList()
			}
		},
		computed: {
			...mapState(['userInfo'])
		},
		methods: {
			...mapActions(['getUserInfo']),
			// 请求后端支付参数
			getPayParams() {
				let _this = this;
				if (this.count == 0) return
				Api.getPayParams({
					count: this.count
				}, (res) => {
					const {
						timestamp,
						nonceStr,
						signType,
						paySign
					} = res.data.data
					uni.requestPayment({
						provider: 'wxpay',
						timeStamp: timestamp,
						nonceStr,
						signType,
						paySign,
						package: res.data.data.package,
						success: function(res) {
							_this.getUserInfo();
							_this.getHisotyPayList()
							console.log('success:' + JSON.stringify(res));
						},
						fail: function(err) {
							console.log('fail:' + JSON.stringify(err));
						}
					});
				})
			},
			// 获取账单列表 2,4
			getHisotyPayList() {
				const time = this.selectTime
				const nowTime = time ?
					`${time.year}-${time.month}-01 00:00:00` : ""
				Api.getHisotyPayList({
					page: this.page,
					pageNum: this.pageNum,
					type: [2, 4].toString(),
					nowTime
				}, (res) => {
					this.payList = res.data.rows
				})
			},
			// 换算成派币
			realPaibi(m) {
				return m * 100
			},
			// 换算成人民币
			realRmb(m) {
				return m / 100
			},
			select(count) {
				this.count = count
			},
			// 显示自定义弹窗
			showPopup() {
				this.show = !this.show
			},
			// 翻页
			scrollBottom() {
				this.page += 1
			},
			// 提交自定义金额
			submit() {
				const value = this.customValue
				if (!value || value < 10) {
					uni.showToast({
						title: '最低0.1个派币（￥10）',
						icon: 'error'
					})
					return
				}
				this.count = this.realPaibi(value)
				this.show = false
			},
			// change tab
			changeTab({
				index
			}) {
				this.current = index
			},
			confirmSelectTime({
				year,
				month,
				day,
				hour,
				minute,
				second
			}) {
				this.selectTime = {
					year,
					month,
					day,
					hour,
					minute,
					second
				}
			},
			getTimeFormat(time) {
				return dayjs(time).format("MM月DD日HH:mm")
			}
		},
		onLoad() {
			this.getHisotyPayList()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>