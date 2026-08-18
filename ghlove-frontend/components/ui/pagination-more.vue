<template>
	<div class="btn-group" v-show="showMoreButton">
		<button type="button" class="btn btn_more" @click="page(nextPageNumber)"><img
				src="/static/images/icon/ic_plus.png" alt="+">more
		</button>
	</div>
</template>
<script>
	module.exports = {
		props: {
			currentPage: {
				type: Number,
				required: true,
				default: function() {
					return 1;
				}
			},
			totalPages: {
				type: Number,
				required: true,
				default: function () {
					return 1;
				}
			}
		},
		data: function() {
			return {

			}
		},
		methods: {

			page: function (page) {
				this.$emit('append', page);
			}
		},
		computed: {

			showMoreButton: function () {

				var flag = this.currentPage < this.totalPages && this.totalPages > 0;

				return flag;
			},

			nextPageNumber: function () {

				var pageNumaber = this.currentPage + 1;

				pageNumaber = pageNumaber < 1 ? 1 : pageNumaber;
				pageNumaber = pageNumaber > this.totalPages ? this.totalPages : pageNumaber;

				return pageNumaber;
			}
		}
	};
</script>