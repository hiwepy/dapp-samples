/*
package com.github.hiwepy.dapp.listener;

*/
/**
 * 服务监听器，继承ApplicationRunner，在spring启动时启动
 * @author liqiang
 *//*

@Component
public class ServiceRunner implements ApplicationRunner {
    */
/**
     * 日志记录
     *//*

    private Logger log = LoggerFactory.getLogger(ServiceRunner.class);


    @Autowired
    private Web3j web3j;

    //如果多个监听，必须要注入新的过滤器
    @Autowired
    private EthFilter uploadProAuth;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        uploadProAuth();
        this.log.info("This will be execute when the project was started!");
    }


    */
/**
     * 收到上链事件
     *//*

    public void uploadProAuth(){
        Event event = new Event("uploadProAuthEvent",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {},
                        new TypeReference<Uint32>() {}));

        uploadProAuth.addSingleTopic(EventEncoder.encode(event));
        log.info("启动监听uploadProAuthEvent");
        web3j.ethLogObservable(uploadProAuth).subscribe(log -> {
            this.log.info("收到事件uploadProAuthEvent");
            EventValues eventValues = staticExtractEventParameters(event, log);
            Trace.UploadProAuthEventEventResponse typedResponse = new Trace.UploadProAuthEventEventResponse();
            typedResponse._fromaddr = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._product_id = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._rand_number =  (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
        });

    }
}*/
