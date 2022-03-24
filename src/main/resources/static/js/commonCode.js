/**
 * 공통코드
 */
let commonCodeObj = {};
let header = $("meta[name='_csrf_header']").attr('content');
let token = $("meta[name='_csrf']").attr('content');
/**
 * 공통코드 조회
 */
function quiz_answer(doc_code, quiz_number, object_num) {
	$.ajax({
		url: "/ajax",
		data: {
			doc_code,
			quiz_number,
			object_num
		},
		dataType: "json",
		success: function(data) {
			if (data.result == "success") {
				alert("정답");
			}
			else {
				alert("오답");
			}
		}
	})
}

function selectCode(in_upper_code) {
	$.ajax({
		url: "/code/upper",
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		data: {
			upper_code_id: in_upper_code
		},
		dataType: "json",
		type: "POST",
		success: function(res) {
			let upperList = res.upperList;
			$.each(upperList, function(obj) {
				$("#upper_code").append('<option value=' + obj.code_id + '>' + obj.code_name + '</option>');
			console.log(obj.code_name);
			});

		},
		error: function(request, error) {
			alert("code=" + request.status + "\n+" + "error:" + error);
		}
	})
}