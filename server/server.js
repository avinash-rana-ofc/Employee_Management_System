var count = 0;
setInterval(async () => {
	const res = await fetch(`https://doe.wbpower.gov.in/cron/expired`);
    //console.log(res)
    console.log(count++)
}, 30000);