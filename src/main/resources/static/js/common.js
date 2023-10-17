//토스트 창
let toastAlert = function (msg, color_val) {
	let toastElList = [].slice.call(document.querySelectorAll('.toast'))
	let toastList = toastElList.map(function (toastEl) {
		return new bootstrap.Toast(toastEl)
	})
	$('.toast-body').text(msg);
	//class="toast hide align-items-center text-white bg-primary border-0"
	if (color_val === 1) {
		$('.toast').removeClass("bg-primary").addClass("bg-danger");
	}
	else {
		$('.toast').removeClass("bg-danger").addClass("bg-primary");
	}
	toastList.forEach(toast => toast.show())
}

//문서 검색
if(document.getElementById('doc_search')) {
	$('#doc_search').autocomplete({
		source: function (request, response) { //source: 입력시 보일 목록
			$.ajax({
				url: "/docSearch"
				, type: "POST"
				, dataType: "JSON"
				, data: {doc_name: $("#doc_search").val()}	// 검색 키워드
				, beforeSend: function (jqXHR, settings) {
					let header = $("meta[name='_csrf_header']").attr("content");
					let token = $("meta[name='_csrf']").attr("content");
					jqXHR.setRequestHeader(header, token);
				}
			}).done(function (rData, textStatus, jqXHR) {
				response(
					$.map(rData.resultList, function (item) {
						return {
							label: item.keyword,    	// 목록에 표시되는 값
							value: item.doc_name 		// 선택 시 input창에 표시되는 값
							//, idx : item.SEQ // index
						};
					})
				);
			}).fail(function (rData, textStatus, errorThrown) {
				alert("오류가 발생했습니다.");
			}).always(function (rData, textStatus, jqXHR) {
			});
		},
		focus: function (evt, ui) { // 방향키로 자동완성단어 선택 가능하게 만들어줌
			return false;
		},
		minLength: 1,// 최소 글자수
		autoFocus: true,
		delay: 300,	//autocomplete 딜레이 시간(ms),
		position: { my : "left top", at: "left bottom" },
		select: function (evt, ui) {
			// 아이템 선택시 실행 ui.item 이 선택된 항목을 나타내는 객체, lavel/value/idx를 가짐
			console.log(ui.item.label);
			//console.log(ui.item.idx);
		},
		close : function(event){
			console.log(event);
		}
	});
}