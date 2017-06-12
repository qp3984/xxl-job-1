import com.xxl.job.executor.service.jobhandler.KylinRESTfulMethod;

public class KylinRESTfulMethodMain {

    public static void main(String[] args) {
        String output;
        KylinRESTfulMethod.login("ADMIN", "KYLIN");

        //------------------------------------------------------------
//        String body = "{\"sql\":\"select count(*) from kylin_sales\",\"offset\":0,\"limit\":50000,\"acceptPartial\":false,\"project\":\"learn_kylin\"}";
//        output = KylinRESTfulMethod.query(body);
//		//--------------------------------------------------------------------

        //------------------------------------------------------------
//		String body = "{\"sql\":\"select * from FACT_\",\"offset\":0,\"limit\":50000,\"acceptPartial\":false,\"project\":\"my_kylin\"}";
//		output = KylinRESTfulMethod.query(body);
//		//--------------------------------------------------------------------
//		output = KylinRESTfulMethod.listQueryableTables("my_kylin");
//		//----------------list all cutes------------------------------
        output = KylinRESTfulMethod.listCubes(0, 15, "sample_cube_1", "learn_kylin");
//		//----------------------------------------------------------------
//		output = KylinRESTfulMethod.getCube("my_kylin_cube");
//		//----------------list my_kylin_cube information--------------
//		output = KylinRESTfulMethod.getCubeDes("sample_cube_1");
//		//----------------get data model-------------------------
//		output = KylinRESTfulMethod.getDataModel("my_kylin_model");
//		//------------------------------------------------------------

//		body = "{\"startTime\":\"\",\"endTime\":\"\",\"buildType\":\"\"}";
//		output = KylinRESTfulMethod.buildCube("my_kylin_cube",body);


        //		//--------------------------------------------------------
//		output = KylinRESTfulMethod.enableCube("my_kylin_cube");
//		//-------------------------------------------------------
//		output = KylinRESTfulMethod.disableCube("my_kylin_cube");
//		//-------------------------------------------------------------------
//		output = KylinRESTfulMethod.purgeCube("my_kylin_cube");
//		//--------------------------------------------------------------------

        //		String jobId="test";
        //		output = KylinRESTfulMethod.resumeJob(jobId);
//		//--------------------------------------------------------------------	
//		jobId="test";
//		output = KylinRESTfulMethod.discardJob(jobId);           
//		//--------------------------------------------------------------------	
//		jobId="test";
//		output = KylinRESTfulMethod.getJobStatus(jobId);
//		//------------------------------------------------------------------		
//		jobId="test";
//		String stepId ="test";
//		output = KylinRESTfulMethod.getJobStepOutput(jobId,stepId);
//		//--------------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTable("FACT_");
//		//---------------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTableInfo("FACT_");
//		//--------------------------------------------------------------
//		output = KylinRESTfulMethod.getHiveTables("my_kylin", true);
//		//-----------------------------------------------------------------
//		output  = KylinRESTfulMethod.loadHiveTables("FACT_", "my_kylin");
//		//--------------------------------------------------------------------

        //output  = KylinRESTfulMethod.wipeCache("METADATA", "my_kylin_cube", "drop");

        System.out.println(output);


        KylinRESTfulMethod.timestampToString(1451606400000L);
        KylinRESTfulMethod.timestampToString(1483228800000L);

    }
}
