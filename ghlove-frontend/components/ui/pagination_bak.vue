<template>
	<ul class="pagination">
		<li class="page-item" v-show="showPreviousButtion">
			<a class="page-link" href="javascript:;" aria-label="Previous" @click="previousPage">
				<span class="sr-only">Previous</span>
			</a>
		</li>
		<li class="page-item" v-for="pageNumber in pageNumbers" :class="{'active' : currentPage == pageNumber}">
			<a class="page-link" href="javascript:;" @click="page(pageNumber)">{{pageNumber}}</a>
		</li>
		<li class="page-item" v-show="showNextButtion">
			<a class="page-link" href="javascript:;" aria-label="Next" @click="nextPage">
				<span class="sr-only">Next</span>
			</a>
		</li>
	</ul>
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
				default: function() {
					return 1;
				}
			},
			pageSize: {
				type: Number,
				default: function() {
					return 5;
				}
			},
			pageTarget: {
				type: String,
				default: function() {
					return 'default';
				}
			}
		},
		data: function() {
			return {

			}
		},
		methods: {

			page: function (page) {

				this.$emit('change', page, this.pageTarget);

			},
			nextPage: function() {

				var page = this.currentPage + 1;

				if (page > this.totalPages) {
					page = this.totalPages;
				}

				this.page(page);
			},
			previousPage: function() {

				var page = this.currentPage - 1;

				if (page < 1) {
					page = 1;
				}

				this.page(page);
			}
		},
		computed : {

			showPreviousButtion: function() {
				return this.currentPage > 1;
			},
			showNextButtion: function() {
				return this.currentPage < this.totalPages;
			},

			pageNumbers: function() {

				var pageNumbers = [];

				try {

					var pageSize = this.pageSize;
					var n = 0;
					var startPage = 0;
					var endPage = 0;
					var halfSizeFloor = 0;
					var currentPage = this.currentPage;
					var totalPages = this.totalPages;


					currentPage = currentPage > totalPages ? totalPages : currentPage;

					n = pageSize / 2;

					halfSizeFloor = Math.floor(n - (1 - (n % 1)) % 1);

					startPage = currentPage < halfSizeFloor + 1 ? 1 : currentPage - halfSizeFloor;
					startPage = currentPage > totalPages - halfSizeFloor ? totalPages - pageSize + 1 : startPage;

					endPage = startPage + pageSize - 1;
					endPage = endPage > totalPages ? totalPages : endPage;

					startPage = totalPages < pageSize ? 1 : startPage;
					endPage = totalPages < pageSize ? totalPages : endPage;

					for (var i = startPage; i <= endPage; i++) {
						pageNumbers.push(i);
					}

					if (!pageNumbers.length > 0) {
						pageNumbers.push(1);
					}

				} catch (e) {
					Saleson.error(e);
				}

				return pageNumbers;
			}
		}
	};
</script>
