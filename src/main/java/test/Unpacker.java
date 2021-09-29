/*
package test;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Unpacker {

    public static void main(String[] args) {
        Unpacker iso = new Unpacker();
        try {
            ISOMsg isoMsg = iso.parseISOMessage();

            iso.printISOMessage(isoMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ISOMsg parseISOMessage() throws Exception {


        String message = "0009886000340080000000100003154076DigiBankGSrcALTOSnk2    741434741434BKECardGroup559                            2021032300864217DNLStructuredData2313410C00000000C00000000C00000000211OriginalPan2166048200011001666214OriginalTrack22246048200011001666=9999101218Postilion:MetaData3173211OriginalPan111214OriginalTrack211117DataPan111215LimitSourceFlag111213OnUsIndicator111222InterBankFundsTransfer11115Payee111222TransactionDescription111212DataRekening11117DataPan2166048200011001666215LimitSourceFlag111213OnUsIndicator112222InterBankFundsTransfer3358<?xml version=\"1.0\" encoding=\"UTF-8\"?> <InterBankFundsTransfer><CentralBankCodeBeneficiary>559</CentralBankCodeBeneficiary><ToAccount>5432154321</ToAccount><BeneficiaryName></BeneficiaryName><CentralBankCodeIssuer>535</CentralBankCodeIssuer><FromAccount>999999999999</FromAccount><SenderName></SenderName><Description>-</Description></InterBankFundsTransfer>15Payee1599999222TransactionDescription2105432154321212DataRekening21054321543219000";
        message = "0430F23A00098A80800A0000004000000000162008899999999999491000000000100089033116060800071016060803310331800000000039050390520000000332600ALTOWBK 3600000070199999020000071003311606080000000090500000000905";

        System.out.printf("Message = %s%n", message);
        try {
//            File file = new File("/home/nacho/java/apache-jmeter-5.3/bin/altopackager.xml");
            File file = new File("/home/nacho/gitbprlestari/backend-service/bank/altopackager.xml");
            InputStream is = new FileInputStream(file);
            GenericPackager packager = new GenericPackager(is);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.unpack(message.getBytes());

            return isoMsg;
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }

    private void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}
*/
