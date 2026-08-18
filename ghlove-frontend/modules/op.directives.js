/* https://beomy.tistory.com/62 */
/* css 에 display:none 이 설정되어 있을 경우 v-show 를 사용해도 block/none 이 진행되지 않아 지시어 추가 */
// 2022-06-29 권세희 - 사용하지 않을 예정입니다. list_area > hide_area > display:none css 가 전역으로 먹여져있어서 임시 사용
Vue.directive("display", function(el, binding) {    // bind & update
    let value = binding.value.value ?? binding.value;
    let attribute = binding.value.attribute ?? "block";
    el.style.display = value ? attribute : "none";
})

/* https://coder-question-ko.com/cq-ko-blog/133559 */
Vue.directive("scroll", {
    inserted: function(el, binding) {
        let f = function (evt) {
            if (binding.value(evt, el)) {
                window.removeEventListener('scroll', f)
            }
        }
        window.addEventListener('scroll', f);
    }
})