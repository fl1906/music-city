<template>
	<cover-view :class="{'music-hover':true,show:hoverMusic}">
		<cover-image src="../../static/hover-music.png" class="image" @click="setPlayOrPause"></cover-image>
		<cover-image src="../../static/hover-music-close.png" class="close" @click="close"></cover-image>
	</cover-view>
</template>

<script>
	import {
		mapState,
		mapActions
	} from "vuex"
	export default {
		computed: {
			...mapState(['hoverMusic', 'audio'])
		},
		methods: {
			...mapActions(['setHoverMusic', 'setPlayOrPause']),
			close() {
				this.setHoverMusic(false)
				this.audio.pause()
			},
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>