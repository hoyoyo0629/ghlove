<template>
    <div class="tab_tit4">
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a href="/notice/list.html" :class="['nav-link', {'active': listType === 'notice'}]">공지사항</a>
            </li>
            <li class="nav-item">
                <a href="/mypage/inquiry.html" :class="['nav-link', {'active': listType === 'qna'}]">1:1문의</a>
            </li>
            <li class="nav-item">
                <a href="/faq/list.html":class="['nav-link', {'active': listType === 'faq'}]">FAQ</a>
            </li>
            <li class="nav-item">
                <a href="/qna/qna-open.html" :class="['nav-link', {'active': listType === 'qna-item'}]">Q&amp;A</a>
            </li>
            <li class="nav-item">
                <a href="/mypage/review.html" :class="['nav-link', {'active': listType === 'review'}]">이용후기</a>
            </li>
            <li class="nav-item">
                <a href="/store/apply.html" :class="['nav-link', {'active': listType === 'store'}]">입점문의</a>
            </li>
        </ul>
    </div>
</template>

<script>
    module.exports = {
        props: {
            listType: String
        }
    }
</script>