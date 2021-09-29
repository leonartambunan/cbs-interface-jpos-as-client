package com.example;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.MUX;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class JposSpringbootApplication {

    //Q2 q2 = new Q2();
	public static void main(String[] args) throws Exception{
        Q2 q2 = new Q2();
        q2.start();
		SpringApplication.run(JposSpringbootApplication.class, args);
	}

//	@Bean
//	public Q2 q2(){
//		Q2 q2 = new Q2();
//		q2.start();
//		return q2;
//	}


	@Bean
	public MUX mux() throws NameRegistrar.NotFoundException {
//		while (!q2().ready()) {
//			ISOUtil.sleep(10);
//		}

		return QMUX.getMUX("my-mux");
	}

	@Autowired
	private MUX mux;

	@GetMapping("echo")
	public String echo() throws ISOException {
		ISOMsg isoMsg =  new ISOMsg();

		isoMsg.setMTI("0800");
		isoMsg.set(11,"000001");
		isoMsg.set(70,"301");

		System.out.println(isoMsg.toString());

		ISOMsg res = mux.request(isoMsg,30000);

		return res.toString();

	}
}
