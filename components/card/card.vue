<template>
	<view :class="{card:true,extra}">
		<view class="card-title u-flex">
			<image src="../../static/line-yellow.png" class="line-yellow"></image>
			<text>{{title}}</text>
		</view>
		<view class="content">
			<slot></slot>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			title: String,
			extra: Boolean
		}
	}
</script>

<style scoped>
	.card {
		width: 100%;
		padding: 24rpx 20rpx 53rpx 26rpx;
		background: linear-gradient(112deg, #332F30, #140B2A);
		border-radius: 20rpx;
		color: #FFF;
		margin-bottom: 37rpx;
	}

	.card .card-title {
		font-size: 30rpx;
		font-family: Source Han Sans CN;
		font-weight: 500;
		color: #FFFFFF;
	}

	.card .card-title .line-yellow {
		width: 5rpx;
		height: 29rpx;
		margin-right: 11rpx;
	}

	.card .extra {
		margin-bottom: 0;
		margin-left: -24rpx;
		background: none;
		padding-bottom: 0;
	}
</style>