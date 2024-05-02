<template>
	<view>
		<u-loading :show="loading" class="loading" v-if="loading"></u-loading>
		<template v-else>
			<slot></slot>
		</template>
	</view>
</template>

<script>
	import {
		mapState,
	} from "vuex"
	export default {
		computed: {
			...mapState(['loading'])
		},
	}
</script>

<style scoped>
	.loading {
		width: 100vw;
		height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>