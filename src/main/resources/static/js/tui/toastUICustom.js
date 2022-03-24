/**
 * Toast UI 테마, 스타일 변경
 */

tui.Grid.applyTheme("default", {
	selection: {
		background: "#e3f4e7",
		border: "#d0d0d0"
	},
	row: {
		even: {
			background: "#f9f9ff"
		}
	},
	cell: {
		normal: {
			background: "#f9f9f9",
			border: "#bababa",
			showVerticalBorder: true
		},
		header: {
			background: "#dedede",
			border: "#bababa",
			showVerticalBorder: true
		},
		rowHeader: {
			border: "#bababa",
			showVerticalBorder: true,
			showHorizontalBorder: true
		}
	},
	outline: {
		border: "#bababa",
		showVerticalBorder: true
	}

});

/**
 * 수정된 데이터 조회 
 */
tui.Grid.prototype.getChangeData = function() {
	this.focusAt(0, 0);
	return this.getData().filter(function(d) {return d.col_status});

}

tui.Grid.setLanguage('ko');


let _createClass = function() {
	function defineProperties(target, props) {
		for (var i = 0; i < props.length; i++) {
			var descriptor = props[i];
			descriptor.enumerable = descriptor.enumerable || false;
			descriptor.configurable = true;

			if ("value" in descriptor) {
				descriptor.writable = true;
			}

			Object.defineProperty(target, descriptor.key, descriptor);
		}
	}

	return function(Constructor, protoProps, staticProps) {
		if (protoProps) {
			defineProperties(Constructor.prototype, protoProps);
		}
		if (staticProps) {
			defineProperties(Constructor, staticProps);
		}
		return Constructor;
	}
}();

/** 
* 상태 셀 렌더러
*/
var CustomStatusRenderer = function() {
	class CustomStatusRenderer {
		constructor(props) {
			_classCallCheck(this, CustomStatusRenderer);

			var el = document.createElement("span");
			el.innerHTML = "";

			this.el = el;
			this.render(props);
		}
	}

	_createClass(CustomStatusRenderer, [
		{
			key: "getElement",
			value: function getElement() {
				return this.el;
			}
		},
		{
			key: "render",
			value: function render(props) {
				if (props.value != null) {
					if (props.value == "I") {
						this.el.innerHTML = "I";
					}
					else if (props.value == "U") {
						this.el.innerHTML = "U";
					}
					else if (props.value == "D") {
						this.el.innerHTML = "D";
					}
				}
			}
		}
	]);

	return CustomStatusRenderer;
}();


function _classCallCheck(instance, Constructor) {
	if (!(instance instanceof Constructor)) {
		throw new TypeError("Cannot call a class a function");
	}
}

/**
 * 그리드 로딩
 */
function fn_gridLoading(grid, onOff) {
	if (onOff) {
		let width = grid.el.clientWidth;
		let height = grid.el.clientHeight;
		let ldsDiv = "<div style='width:" + width + "px; height:" + height + "px;' class='grid_loading'><div class='tui-grid-layer-state-loading'></div></div>";
		$(grid.el).prepend(ldsDiv);
	} else {
		$(grid.el).find(".grid_loading").remove();
	}
}


/**
 * 그리드 생성
 * @param options 옵션
 * @param callback 콜백함수
 * @return
 */

function fn_createCustomTuiGrid(options, callback) {
	let grid = new tui.Grid(options);
	grid.beforeCellValue = null;

	//ajax call
	if (options.customAjaxUrl) {
		grid.customAjaxDataLoad = (param, callback) => {
			$.ajax({
				url: options.customAjaxUrl,
				data: param,
				dataType: "json",
				type: "POST",
				beforeSend: function() {
					fn_gridLoading(grid, true);
				},
				success: function(data) {
					if (typeof callback === "function") {
						callback(data);
					}
				},
				error: function(_reuqest, _status) {
					alert("에러발생");
				}
			}).always(function() {
				fn_gridLoading(grid, false);
			});
		}
	}

	//그리드 셀 변경 시작 이벤트
	grid.on("editingStart", function(evt) {
		grid.beforeCellValue = evt.value;

		if (grid.beforeCellValue == null) {
			grid.beforeCellValue = "";
		}
	});

	//그리드 셀 변경 완료 이벤트
	grid.on("editingFinish", function(evt) {
		if (status != "I") {
			if (grid.beforeCellValue != evt.value) {
				grid.setValue(evt.rowKey, "status", "U", false);
			}
		}
		grid.beforeCellValue = null;
	});

	grid.on("onGridUpdated", function(evt) {
		fn_gridLoading(grid, false);
	});

	if (typeof callback === "function") {
		callback(grid);
	}


	return grid;
}

/**
 * 변수의 널값 체크
 */
var ComUtil = new Object();

ComUtil.isEmpty = function(value) {
	if (value == "" || value == null || value == undefined || value == 'undefined' || (value != null && typeof value == "object" && !Object.keys(value).length)) {
		return true;
	} else {
		return false;
	}
}

/**
* 그리드 유효성 검사
 */

function fn_gridValidation(changeData, columns, grid) {
	//수정중인 셀이 존재하는 경우
	if (grid.beforeCellValue != null) {
		alert("그리드내 작성중인 셀이 존재합니다.");
	}

	if (changeData.length == 0) {
		alert("변경되거나 추가된 데이터가 없습니다.");
	}

	var result = {
		isValidate: true,
		errorMsg: "",
		errorRow: null,
		errorCol: ""
	}

	$.each(changeData, function(key, data) {
		$.each(columns, function(idx, obj) {
			//필수확인
			if (obj.required) {
				//필수확인 조건이 있는 경우
				if (typeof obj.condition === "function") {
					if (obj.condition(data)) {

						//추가 연산
						if (typeof obj.expandCalc === "function") {
							if (obj.expandCalc(data)) {
								result.isValidata = false;

								if (obj.expandCaclMsg === "" || obj.expandCaclMsg === null || obj.expandCaclMsg === "undefined") {
									result.errorMsg = "에러 발생";
								} else {
									result.errorMsg = obj.expandCalcMsg;
								}

								result.errorRow = data.rowKey;
								result.errorCol = obj.name;
								return false;
							}
						}

						if (typeof data[obj.name] == "undefined" || data[obj.name] == null || data[obj.name] == "") {
							result.isValidata = false;
							result.errorMsg = ("에러발생");
							result.errorRow = data.rowKey;
							result.errorCol = obj.name;
							return false;
						}
					}
				} else {
					if (typeof data[obj.name] == "undefined" || data[obj.name] == null || data[obj.name] == "") {
						result.isValidata = false;
						result.errorMsg = ("에러발생");
						result.errorRow = data.rowKey;
						result.errorCol = obj.name;
						return false;
					}
				}
			}

			//숫자 확인
			if (obj.isNumber) {
				if (isNaN(Number(data[obj.name]))) {
					result.isValidate = false;
					result.errorMsg = obj.header + "은(는) 숫자만 입력 가능합니다.";
					result.errorRow = data.rowKey;
					result.errorCol = obj.name;
					return false;
				}
			}

			//길이 확인
			if (obj.editor === "text" && data[obj.name] != null) {
				if (obj.length < data[obj.name].length) {
					result.isValidate = false;
					result.errorMsg = obj.header + "은(는) " + obj.length + "자리 이상 입력할 수 없습니다.";
					result.errorRow = data.rowKey;
					result.errorCol = obj.name;
					return false;
				}
			}
		});

		if (!result.isValidate) {
			alert(result.errorMsg);
			grid.startEditing(result.errorRow, result.errorCol, true);
			return false;
		}
	});
	return result.isValidate;

}