<template>
	<view class="security-fund">
		<view class="promise u-flex">
			<image src="../../static/ac-fee-icon.png"></image>
			活动保障金
		</view>
		<view class="paibi u-flex">
			<image src="../../static/paibi.png"></image>
			<text class="tag">X</text>
			<input v-model="fund" />
			<text class="desc">余额{{userInfo.money}}派币</text>
			<text class="charge" @click="toChargePage">充值</text>
		</view>
		<view class="fee">
			<view class="u-flex">
				<image src="../../static/ac-pay.png"></image>
				活动经费
			</view>
			<u-select v-model="show" :list="list" @confirm="confirm"></u-select>
			<view class="input u-flex" @click="openFee">
				<text>{{current?current.label:""}}</text>
				<image class="xiala" src="../../static/xiala.png"></image>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapActions
	} from "vuex"
	export default {
		watch: {
			fund(val) {
				this.$emit('getFund', val)
			},
			current(val) {
				this.$emit('getBudget', val)
			}
		},
		computed: {
			...mapState(['userInfo'])
		},
		data() {
			return {
				paibi: 0,
				fund: null,
				show: false,
				current: null,
				list: [{
						value: 0,
						label: 'AA制'
					},
					{
						value: 1,
						label: '我买单'
					}, {
						value: 2,
						label: '对方买单'
					}
				],
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			confirm(value) {
				this.current = value[0]
			},
			// 前往充值页面
			toChargePage() {
				uni.navigateTo({
					url: '/pages/my/paibi/index'
				})
			},
			openFee() {
				this.show = true
			}
		},
		mounted() {
			this.getUserInfo()
		}
	}
</script>

<style scoped>
	.security-fund .promise image {
		width: 29rpx;
		height: 34rpx;
		margin-right: 16rpx;
	}

	.security-fund .paibi {
		margin-top: 33rpx;
	}

	.security-fund .paibi image {
		width: 61rpx;
		height: 61rpx;
	}

	.security-fund .paibi .tag {
		margin-left: 26rpx;
		margin-right: 27rpx;
		font-size: 22rpx;
	}

	.security-fund .paibi input {
		width: 80rpx;
		height: 80rpx;
		border: 2px solid #C6C6C6;
		border-radius: 10rpx;
		padding-left: 20rpx;
	}

	.security-fund .paibi .desc,
	.security-fund .paibi .charge {
		font-size: 22rpx;
		font-family: Source Han Sans CN;
		font-weight: 400;
		color: #A4A4A4;
		margin-top: 40rpx;
		margin-left: 14rpx;
	}

	.security-fund .paibi .charge {
		color: #84E0EB;
		border-bottom: 1px soild #85E0EB;
	}

	.security-fund .fee {
		margin-top: 31rpx;
	}

	.security-fund .fee image {
		width: 35rpx;
		height: 35rpx;
		margin-right: 12rpx;
	}

	.security-fund .fee .u-flex {
		margin-bottom: 23rpx;
	}

	.security-fund .fee .input {
		position: relative;
		height: 70rpx;
		border: 2px solid #C6C6C6;
		border-radius: 10rpx;
		padding-left: 20rpx;
		justify-content: center;
	}

	.security-fund .fee .input .xiala {
		position: absolute;
		width: 23rpx;
		height: 15rpx;
		text-align: right;
		right: 43rpx;
	}
</style>