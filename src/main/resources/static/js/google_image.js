// 구글 이미지 등록
let img_convert = function () {
	let gdUrl = $("#img_url");
	let result;
	if (!isValidUrl(gdUrl.val())) {
		alert("You have inputted invalid path.");
		gdUrl.val("");
		return;
	}
	const url = new URL(gdUrl.val());
	let urlParam = url.searchParams;
	let gdId = urlParam.get('id');
	let prefix = "http://drive.google.com/uc?export=view&id=";
	result = (prefix + gdId);
	document.getElementById('img_target').value = result;

	let clipboard = new Clipboard('.btn');

	clipboard.on('success', function (e) {
		/*console.info('Action:', e.action);
		console.info('Text:', e.text);
		console.info('Trigger:', e.trigger);*/

		e.clearSelection();
	});

	clipboard.on('error', function (e) {
		console.error('Action:', e.action);
		console.error('Trigger:', e.trigger);
	});

}

// validity check. ref: https://gist.github.com/jlong/2428561
function isValidUrl(url) {
	// to be impl...
	let parser = document.createElement('a');
	parser.href = url;

	if (url === '' || parser.hostname !== "drive.google.com")
		return false;

	return true;
}