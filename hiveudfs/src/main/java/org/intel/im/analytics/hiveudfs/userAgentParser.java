package org.intel.im.analytics.hiveudfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

import static org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category.LIST;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.DoubleObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.LongObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.LongWritable;
import net.sf.uadetector.service.UADetectorServiceFactory;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;

/**
 *
 * @author Lilun
 */
@Description(name = "user agent parser",
    value = "_FUNC_(array(struct1,struct2,...), string myfield) - "
    + "returns the passed array struct which gives brower, OS information " ,
    extended = "Example:\n"
    + "  > SELECT _FUNC_(user_agent) FROM src LIMIT 1;\n"
    + " 'b' ")

public class userAgentParser extends GenericUDF {
    protected ObjectInspector[] argumentOIs;
    private ArrayList ret;
    
    LongObjectInspector loi;
    LongObjectInspector elOi;
 
    @Override
    public ObjectInspector initialize(ObjectInspector[] ois) throws UDFArgumentException {
    // all common initialization
    argumentOIs = ois;
    
    if( ois[0].getCategory() != ObjectInspector.Category.PRIMITIVE){
    	throw new UDFArgumentTypeException(0,"The arguments should be "
    	          + "Primative type"
    	          + " but " + ois[0].getTypeName() + " is found");
    }
    
    if(ois.length != 2 ) {
        throw new UDFArgumentException("2 arguments needed, found " + ois.length );
    }
    
    loi = ((LongObjectInspector)ois[0]);
    elOi = ((LongObjectInspector)ois[1]);
    
    ret = new ArrayList();    
    ArrayList structFieldNames = new ArrayList();
    ArrayList structFieldObjectInspectors = new ArrayList();

    structFieldNames.add("starttime");
    structFieldNames.add("endtime");
    structFieldNames.add("viewedtime");
 

    // To get instances of PrimitiveObjectInspector, we use the PrimitiveObjectInspectorFactory
    structFieldObjectInspectors.add( PrimitiveObjectInspectorFactory.writableLongObjectInspector);
    structFieldObjectInspectors.add( PrimitiveObjectInspectorFactory.writableLongObjectInspector);
    structFieldObjectInspectors.add( PrimitiveObjectInspectorFactory.writableLongObjectInspector);
    

    // Set up the object inspector for the struct<> for the output
    StructObjectInspector si2;
    si2 = ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames, structFieldObjectInspectors);

    // Set up the list object inspector for the output, and return it
    ListObjectInspector li2;
    li2 = ObjectInspectorFactory.getStandardListObjectInspector( si2 );
    return li2;
    }
 
    // to sort a list , we must supply our comparator

    @Override
    public Object evaluate(DeferredObject[] dos) throws HiveException {
    // get list
    if(dos==null || dos.length != 2) {
        throw new HiveException("received " + (dos == null? "null" :
            Integer.toString(dos.length) + " elements instead of 2"));
    }
    	
      long strt_val =  (Long) loi.getPrimitiveJavaObject(dos[0].get());
      long end_val = (Long) elOi.getPrimitiveJavaObject(dos[1].get());
      long view_val = 0;
      
      long startBckt = strt_val - strt_val % (60 * 1000);
      long endBckt = end_val - end_val % (60 * 1000);
      
      Object[] rtrn_set;

      ret = new ArrayList();
      
      do{
    	  rtrn_set = new Object[3];
    	  rtrn_set[0] =  new LongWritable(startBckt);
    	  rtrn_set[1] =  new LongWritable(startBckt + (60 * 1000));
      
    	  if(end_val > startBckt + (60 * 1000)){
    		  rtrn_set[2] =  new LongWritable(startBckt + (60 * 1000) - strt_val);
    	  }else{
    		  rtrn_set[2] =  new LongWritable(end_val - strt_val);
    	  }
    	  ret.add(rtrn_set);
    	  startBckt = startBckt + (60 * 1000);
    	  strt_val = startBckt;
      }while(end_val > startBckt);
    return ret;
    }
 
	@Override
    public String getDisplayString(String[] children) {
    return  (children == null? null : this.getClass().getCanonicalName() + "(" + children[0] + "," + children[1] + ")");
    }
 
}