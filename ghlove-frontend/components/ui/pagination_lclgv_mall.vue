<template>
	<div class="pagination_ali" v-show="totalPages > 0">
		<ul class="pagination-frame">
			<!-- <li @click="changeCurrentPage((currentPage - pageCount) > 1 ? currentPage - pageCount : 1)"> -->
			<li>
				<button type="button" class="fist arrow_btn" @click="firstPage" ref="firstPageBtn" :disabled="currentPage <= 1">
					<img src="/static/images/icon/paging_btn-first.png" alt="처음">
				</button>
			</li>
			<!-- <li class="page-left-btn" @click="changeCurrentPage((currentPage - 1) > 1 ? currentPage - 1 : 1)"> -->
			<li>
				<button class="prev arrow_btn" type="button" @click="previousPage" ref="previousPageBtn" :disabled="currentPage <= 1">
					<img src="/static/images/icon/paging_btn-prev.png" alt="이전">
				</button>
			</li>

			<li v-for="pageNumber in pageNumbers" :class="{ 'selected-page': pageNumber === currentPage }">
				<button class="page-text" type="button" @click="page(pageNumber)">{{ pageNumber }}</button>
			</li>

			<!-- <li class="page-right-btn" @click="changeCurrentPage((currentPage + 1) < maxPage ? currentPage + 1 : maxPage)"> -->
			<li>
				<button class="next arrow_btn" type="button" @click="nextPage" ref="nextPageBtn" :disabled="currentPage >= totalPages">
					<img src="/static/images/icon/paging_btn-next.png" alt="다음">
				</button>
			</li>
			<!-- <li @click="changeCurrentPage((currentPage + pageCount) < maxPage ? currentPage + pageCount : maxPage)"> -->
			<li>
				<button class="last arrow_btn" type="button" @click="lastPage" ref="lastPageBtn" :disabled="currentPage >= totalPages">
					<img src="/static/images/icon/paging_btn-last.png" alt="마지막">
				</button>
			</li>
		</ul>
	</div>
</template>
<script>
module.exports = {
	props: {
		currentPage: {
			type: Number,
			required: true,
			default: function () {
				return 1;
			}
		},
		totalPages: {
			type: Number,
			required: true,
			default: function () {
				return 1;
			}
		},
		pageSize: {
			type: Number,
			default: function () {
				return 5;
			}
		},
		pageTarget: {
			type: String,
			default: function () {
				return 'default';
			}
		}
	},
	data() {
		return {

		}
	},
	computed: {
		pageNumbers: function () {
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

				startPage = currentPage < halfSizeFloor +1 ? 1 : currentPage - halfSizeFloor;
				startPage = currentPage > totalPages - halfSizeFloor ? totalPages - pageSize + 1 : startPage;

				endPage = startPage + pageSize -1 ;
				endPage = endPage > totalPages ? totalPages : endPage;

				startPage = totalPages < pageSize ? 1 : startPage;
				endPage = totalPages < pageSize ? totalPages : endPage;

				for (var i = startPage; i <= endPage; i++) {
					pageNumbers.push(i);
				}

				if (!pageNumbers.length > 0) {
					pageNumbers.push(1);
				}

				// 커서 이동, 클릭 되지 않게 세팅
				/*if (startPage == currentPage) {
					if (this.$refs.firstPageBtn) {
						this.$refs.firstPageBtn.setAttribute('disabled', 'disabled');
					}
					if (this.$refs.previousPageBtn) {
						this.$refs.previousPageBtn.setAttribute('disabled', 'disabled');
					}
				} else {
					if (this.$refs.firstPageBtn) {
						this.$refs.firstPageBtn.removeAttribute('disabled');
					}
					if (this.$refs.previousPageBtn) {
						this.$refs.previousPageBtn.removeAttribute('disabled');
					}
				}
				if (currentPage == endPage) {
					if (this.$refs.nextPageBtn) {
						this.$refs.nextPageBtn.setAttribute('disabled', 'disabled');
					}
					if (this.$refs.lastPageBtn) {
						this.$refs.lastPageBtn.setAttribute('disabled', 'disabled');
					}
				} else {
					if (this.$refs.nextPageBtn) {
						this.$refs.nextPageBtn.removeAttribute('disabled', 'disabled');
					}
					if (this.$refs.lastPageBtn) {
						this.$refs.lastPageBtn.removeAttribute('disabled', 'disabled');
					}
				}*/
			} catch (e) {
				Saleson.error(e);
			}

			return pageNumbers;
		},
	},
	methods: {
		page: function (page) {
			this.$emit('change', page, this.pageTarget);
		},
		nextPage: function () {
			var page = this.currentPage + 1;

			if (page > this.totalPages) {
				page = this.totalPages;
			}

			this.page(page);
		},
		previousPage: function () {
			var page = this.currentPage - 1;

			if (page < 1) {
				page = 1;
			}

			this.page(page);
		},
		firstPage: function () {
			var page = 1;
			if (page < 1) {
				page = 1;
			}

			this.page(page);
		},
		lastPage: function () {
			var page = this.totalPages;
			if (page > this.totalPages) {
				page = this.totalPages;
			}
			this.page(page);
		},
	}
}

</script>