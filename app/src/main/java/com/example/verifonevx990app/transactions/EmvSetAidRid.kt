package com.example.verifonevx990app.transactions

import android.os.RemoteException
import android.util.Log
import com.example.verifonevx990app.utils.DRLUtil
import com.example.verifonevx990app.vxUtils.AppPreference
import com.example.verifonevx990app.vxUtils.convertStr2Nibble2Str
import com.vfi.smartpos.deviceservice.aidl.IEMV
import com.vfi.smartpos.deviceservice.constdefine.ConstIPBOC


class EmvSetAidRid(private var ipboc: IEMV?, private var updateCVMValue: String, private var ctlsUpdateTransLimit: String) {
    private val TAG = "EMV-SetAidRid"
    private var ctlsVal: String? = null
    private var cvmVal: String? = null

    init {
        Log.d("CTLS:- ", ctlsUpdateTransLimit)
        ctlsVal = convertStr2Nibble2Str(ctlsUpdateTransLimit)
        cvmVal = convertStr2Nibble2Str(updateCVMValue)
    }

    /**
     * @brief set, update the AID
     *
     * In this demo, there're 2 way to set the AID
     * 1#, set each tag & value
     * 2#, set one tlv string
     * in the EMVParamAppCaseA, you can reset the tag or value in EMVParamAppCaseA.append
     * \code{.java}
     * \endcode
     * @version
     * @see EMVParamAppCaseA
     */
    fun setAID(type: Int) {
        var isSuccess: Boolean
        if (type == ConstIPBOC.updateRID.operation.clear) {
            // clear all AID
            isSuccess = false
            try {
                isSuccess = ipboc!!.updateAID(3, 1, null)
                Log.d("TAG", "Clear AID (smart AID):$isSuccess")
                isSuccess = ipboc!!.updateAID(3, 2, null)
                Log.d("TAG", "Clear AID (CTLS):$isSuccess")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return
        }


        try {
            val drlDataList = DRLUtil(updateCVMValue, ctlsUpdateTransLimit).amexdrl
            for (drlData in drlDataList) {
                isSuccess = ipboc!!.updateVisaAPID(2, drlData)
                Log.d("TAG", "Clear UpdateVisAPIDAID:$isSuccess")
            }
            for (drlData in drlDataList) {
                isSuccess = ipboc!!.updateVisaAPID(1, drlData)
                Log.d("TAG", "Add UpdateVisAPIDAID:$isSuccess")
            }
        }
        catch (ex: Exception){
            ex.printStackTrace()
        }

        // 2# way of setting the AID
        // given the AID string to set. You can change the EMVParamAppCaseA to check each Tag & Value, and modify the tag or value if need
        // hardcoding the AID string
        val AID_SmartCard =
            arrayOf(
// =============================================Visa Contact card Aids starts============================================================================================
                // Visa credit or debit	10 10
                // Visa credit or debit	10 10

                        "9F3303E020C8" +     //Terminal capability
                        "DF1A06000000012000" +
                        "9F0607A0000000031010" + //Application Aid
                        "9F40056F00F0F001" +  //Additional terminal cpability
                        "DF010100" +
                        "DF2006009999999999" +
                        "DF14039F3704" +
                        "9F660432004000" + //Terminal transaction qualifier(TTQ)
                        "DF170101" +   // DDA public module length
                        "9F09020096" + //Application version number
                        "5F2A020901" + // Transaction currency code
                        "DF1B06000000000000" +
                        "9F350122" +   //Terminal type
                        "DF1205FC60ACF800" +
                        "9F1B0400000000" + //Terminal floor limit
                        "9F1A020158" +     //Terminal country code
                        "DF2106009999999999" +
                        "DF160199" +
                        "DF150400049444" +
                        "DF1105FC6024A800" +
                        "9F08020140" +      //Application version
                        "DF1906000000000000" +
                        "DF13050010000000",  //Public modulus

                // Visa credit or debit	10 10
                // Visa credit or debit	10 10
                "9F0607A0000000031010" +
                        "DF2006009999999999" +
                        "DF010100" +
                        "DF140111" +
                        "DF170101" +
                        "9F09020096" + //Application version number
                        "DF180101" +
                        "DF12050000000000" +
                        "9F1B0400000000" + //Terminal floor limit
                        "DF160101" +
                        "DF150400000000" +
                        "DF1105C000000000" +
                        "DF1906000000000000" +
                        "9F7B06000000000000" + //Terminal transaction limit
                        "DF13050000000000",  //DDA public module

                // Visa credit or debit	10 10
                // Visa credit or debit	10 10
                "9F0608A000000003101001" +
                        "DF2006009999999999" +
                        "DF010100" +
                        "DF140111" +
                        "DF170101" +
                        "9F09020096" + //Application version number
                        "DF180101" +
                        "DF1205D84000F800" +
                        "9F1B0400000000" +
                        "DF160101" +
                        "DF150400000000" +
                        "DF1105D84004A800" +
                        "DF1906000000000000" +
                        "9F7B06000000000000" +
                        "DF13050010000000",

                // Visa credit or debit	10 10
                // Visa credit or debit	10 10
                "9F0608A000000003101002" +
                        "DF2006009999999999" +
                        "DF010100" +
                        "DF140111" +
                        "DF170101" +
                        "9F09020096" + //Application version number
                        "DF180101" +
                        "DF1205D84000F800" +
                        "9F1B0400000000" +
                        "DF160101" +
                        "DF150400000000" +
                        "DF1105D84004A800" +
                        "DF1906000000000000" +
                        "9F7B06000000000000" +
                        "DF13050010000000",

                // Visa Electron	20 10
                // Visa Electron	20 10
                "9F0607A0000000032010" +
                        "DF2006009999999999" +
                        "DF010100" +
                        "DF140111" +
                        "DF170101" +
                        "9F09020096" + //Application version number
                        "DF180101" +
                        "DF1205D84000F800" +
                        "9F1B0400000000" +
                        "DF160101" +
                        "DF150400000000" +
                        "DF1105D84004A800" +
                        "DF1906000000000000" +
                        "9F7B06000000000000" +
                        "DF13050010000000",
// =============================================Visa Contact card Aids Ends============================================================================================
                // =================================Masters card Aids starts========================================================
                // MasterCard credit or debit	10 10
                "9F0607A0000000041010" +
                        "DF2006009999999999DF010100DF140111DF1701019F09020004DF180101DF1205F85080F8009F1B0400000000DF160101DF150400000000DF1105FC5080A000DF19060000000000009F7B06000000001000DF13050000400000",
                // MasterCard(debit card)_	30 60
                "9F0607A0000000043060" +
                        "DF2006009999999999DF010100DF140111DF1701019F09020004DF180101DF1205F85080F8009F1B0400000000DF160101DF150400000000DF1105FC5080A000DF19060000000000009F7B06000000000000DF13050000400000",
                // MasterCard
                "9F0607A0000000044010" +
                        "DF2006009999999999DF010100DF140111DF1701019F09020004DF180101DF1205F85080F8009F1B0400000000DF160101DF150400000000DF1105FC5080A000DF19060000000000009F7B06000000000000DF13050000400000",

                // =================================Masters card Aids Ends========================================================

                // =================================PBOC card Aids starts========================================================
                // PBOC
                "9F0608A000000333010103" +
                        "DF2006009999999999DF010100DF14039F3704DF1701209F09020020DF180101DF1205DC4004F8009F1B0400000064DF160150DF150400000028DF1105DC4000A800DF19060000000000009F7B06000000100000DF13050010000000",
                "9F0608A000000333010101" +
                        "DF2006009999999999DF010100DF14039F3704DF1701999F09020020DF180101DF1205DC4004F8009F1B0400000064DF160199DF150400000000DF1105DC4000A800DF19060000000000009F7B06000000100000DF13050010000000",
                "9F0608A000000333010106" +
                        "DF2006009999999999DF010100DF14039F3704DF1701999F09020020DF180101DF1205DC4004F8009F1B0400000064DF160199DF150400000000DF1105DC4000A800DF19060000000000009F7B06000000100000DF13050010000000",
                "9F0608A000000333010102" +
                        "DF2006009999999999DF010100DF14039F3704DF1701209F09020020DF180101DF1205DC4004F8009F1B0400000064DF160150DF150400000000DF1105DC4000A800DF19060000000000009F7B06000000100000DF13050010000000",

                // =================================PBOC card Aids starts========================================================


                // =================================JCB card Aids starts========================================================
                // jCB
                "9F0607A0000001523010" +
                        "DF2006009999999999DF010100DF140111DF1701019F09020001DF180101DF1205D84004F8009F1B0400000000DF160101DF150400000000DF1105D84004A800DF19060000000000009F7B06000000000000DF13050010000000",

                // =================================JCB card Aids starts========================================================


                "9F0606F00000002501" +
                        "DF2006009999999999DF010100DF14039F3704DF1701009F09020001DF180100DF1205CC000000009F1B0400000000DF160101DF150400000000DF1105CC00000000DF19060000000000009F7B06000000100000DF130500000000009F1A020356",
                "9F0606A00000002501" +
                        "DF2006009999999999DF010100DF14039F3704DF1701999F09020001DF180101DF1205CC000080009F1B0400000000DF160199DF150400000000DF1105CC00000000DF19060000000000009F7B06000010000000DF130500000000009F1A020356",
                "9F0607A0000003241010" +
                        "DF2006009999999999DF010100DF14039F3704DF1701999F09020001DF180101DF1205FCE09CF8009F1B0400000000DF160199DF150400000000DF1105DC00002000DF19060000000000009F7B06000000000000DF130500100000009F1A020356",
                "9F0605A000000000" +
                        "DF2006009999999999DF010100DF140111DF1701019F09020001DF180100DF120500000000009F1B0400000064DF160101DF150400000001DF11050000000000DF19060000000000009F7B06000000000100DF130500000000009F1A020356"
            )
        val AID_CTLS_Card = arrayOf(

            //American express Aid's
            "9F0606F00000002501" +
                    "DF0306009999999999DF2006009999999999DF010100DF14039F37049F6601225F2A020356DF1701009F09020001DF180100DF1205CC000000009F1B04000000009F1A020356DF2106000000100000DF160101DF150400000000DF1105CC00000000DF0406000000000501DF1906000000000000DF13050000000000",
            "9F0606A00000002501" +     // AID
                    "DF0306009999999999" +
                    "DF2006${ctlsVal}" + //Contact less Maximum Transaction Limit
                    "DF010100" +     //Application id
                    "DF14039F3704" + //DDol //Dynamic Data
                    "9F6604A6004000" + ///terminal transaction attribute 86004000  // "9F660426000080 //TTQ
                    "5F2A020356" +   //  Transaction currency code
                    "DF170199" +    //The target percentage randomly choosen
                    "9F09020001" +  // Application version
                    "DF180101" +    //Online pin
                    "DF1205C400000000" +  //TAC Online
                    "9F1B0400000000" +    //Minimum Limit
                    "9F1A020356" +        //   Terminal Country code
                    "DF2106${cvmVal}" +  //Terminal cvm(cardholder verification methods) quota
                    "DF160199" +              //Bias select the maximum percentage of target
                    "DF150400000000" +       //offset Randomly selected thresold
                    "DF11050000000000" +     //TAC Default
                    "DF0406000000000000" +   //
                    "DF1906000000000000" +   //Contact less offline minimum
                    "DF13050000000000",    //TAC Refuse

            /*  "9f0607A0000001523010"+*/
            "9F0607A0000003241010" +
                    "DF0306009999999999" +
                    "DF2006009999999999" +
                    "DF010100DF14039F37049F6601265F2A020356DF1701999F09020001DF180101DF1205FCE09CF8009F1B04000000009F1A020356DF2106000000000000DF160199DF150400000000DF1105DC00002000DF0406000000000000DF1906000000000000DF13050010000000",

//=============================================Visa Contactless card Aids starts============================================================================================
                    // Visa, Plus	80 10
                   // Visa, Plus	80 10

                    "9F3303E0F0C8"+
                    "9F40056200002001"+
                    "97099F02065F2A029A0390"+
                    "9F40056F00F0F001" + //additional terminal capability
                    "9F0607A0000000038010" +
                    "DF0306009999999999" +
                    "DF2006009999999999" +
                    "DF010100" +
                    "DF14039F3704" +
                    "9F660100" +
                    "5F2A020356" +
                    "DF170101" +
                            "9F09020096" + //Application version number
                    "DF180101" +
                    "DF1205D84004F800" + //Tac online
                    "9F1B0400000000" +
                    "9F1A020356" +
                    "DF2106000000000000" +
                    "DF160101" +
                    "DF150400000000" +
                    "DF1105D84004A800" +
                    "DF0406000000000000" +
                    "DF1906000000000000" +
                    "DF13050010000000",


                    "9F3303E0F0C8"+
                    "9F40056200002001"+
                    "97099F02065F2A029A0390"+
                    "9F0607A0000000031010" +
                    "DF0306009999999999" +
                    "DF2006009999999999" + //Contact less Maximum Transaction Limit
                    "DF010100" +           //Application id
                    "DF14039F3704" +       //DDOL (Dynamic data authetication...)
                    "9F6604F6004000" +           //Teminal transaction qualifier
                    "5F2A020356" +         //  Transaction currency code
                    "DF170101" +           //The target percentage randomly choosen
                            "9F09020096" + //Application version number
                    "DF180101" +           //Online pin
                    "DF1205A0109C9800" +   //TAC online
                    "9F1B0400000000" +      //Minimum Limit //floor limit
                    "9F1A020356" +          //Terminal Country code
                    "DF2106000000001000" +  //Terminal cvm(cardholder verification methods) quota
                    "DF160101" +             //Bias select the maximum percentage of target
                    "DF150400000000" +       //offset Randomly selected thresold
                    "DF1105A4109C0000" +     //TAC Default
                    "DF0406000000000000" +
                    "DF1906000000000000" +    //Contact less offline minimum
                    "DF13055C40000000",       //TAC Refuse

                    "9F3303E0F0C8"+
                    "9F40056200002001"+
                     "97099F02065F2A029A0390"+
                    "9F0608A000000003101001" +
                    "DF2006009999999999" +
                    "DF010100" +
                    "DF14039F3704" +
                    "9F660100" +
                    "5F2A020156" +
                    "DF170101" +
                            "9F09020096" + //Application version number
                    "DF180101" +
                    "DF1205A0109C9800" +
                    "9F1B0400000000" +
                    "9F1A020356" +
                    "DF2106000000001000" +
                    "DF160101" +
                    "DF150400000000" +
                    "DF1105A4109C0000" +
                    "DF0406000000000000" +
                    "DF1906000000000000" +
                    "DF13055C40000000",

                    "9F3303E0F0C8"+
                    "9F40056200002001"+
                    "97099F02065F2A029A0390"+
                    "9F0608A000000003101002" +
                    "DF2006009999999999" +
                    "DF010100" +
                    "DF14039F3704" +
                    "9F660100" +
                    "5F2A020156" +
                    "DF170101" +
                            "9F09020096" + //Application version number
                    "DF180101" +
                    "DF1205A0109C9800" +
                    "9F1B0400000000" +
                    "9F1A020356" +
                    "DF2106000000001000" +
                    "DF160101" +
                    "DF150400000000" +
                    "DF1105A4109C0000" +
                    "DF0406000000000000" +
                    "DF1906000000000000" +
                    "DF13055C40000000"
// =============================================Visa Conatcless card Aids Ends============================================================================================

        )

        try {
            if (ipboc != null) {
                isSuccess = ipboc!!.updateAID(3, 1, null)
                Log.d("TAG", "Clear AID (smart AID):$isSuccess")
                isSuccess = ipboc!!.updateAID(3, 2, null)
                Log.d("TAG", "Clear AID (CTLS):$isSuccess")
          //      VFService.showToast("AID & RID Configured Successfully!!!")
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }


        // =================================Masters card Aids starts========================================================

                // MasterCard credit or debit	10 10
            "9F0607A0000000041010" +
                    "DF0306009999999999DF2006009999999999DF010100DF14039F37049F6601005F2A020156DF1701019F09020004DF180101DF1205A0109C98009F1B043B9ACA009F1A020156DF2106000000001000DF160101DF150400000000DF1105A4109C0000DF0406000000000000DF1906000000000000DF13055C40000000"

            // =================================Masters card Aids Ends========================================================


            // =================================PBOC card Aids Ends========================================================
            // PBOC
            "9F0608A000000333010102" +
                    "DF0306009999999999DF2006009999999999DF010100DF14039F37049F6601265F2A020156DF1701999F09020020DF180101DF1205DC4004F8009F1B04000000649F1A020156DF2106000000100000DF160199DF150400000000DF1105DC4000A800DF0406000000000000DF1906000000000000DF13050010000000"
            "9F0608A000000333010101" +
                    "DF0306009999999999DF2006009999999999DF010100DF14039F37049F6601265F2A020156DF1701999F09020020DF180101DF1205DC4004F8009F1B04000000649F1A020156DF2106000000100000DF160199DF150400000000DF1105DC4000A800DF0406000000000000DF1906000000000000DF13050010000000"
            "9F0608A000000333010106" +
                    "DF0306009999999999DF2006009999999999DF010100DF14039F37049F6601265F2A020156DF1701999F09020020DF180101DF1205DC4004F8009F1B04000000649F1A020156DF2106000000100000DF160199DF150400000000DF1105DC4000A800DF0406000000000000DF1906000000000000DF13050010000000"
            // =================================PBOC card Aids Ends========================================================

        val smartCardAidType = ConstIPBOC.updateAID.aidType.smart_card
        val ctlsAidType = ConstIPBOC.updateAID.aidType.contactless
        val smartCardDataList = AID_SmartCard.toMutableList()
        val ctlsCardDataList = AID_CTLS_Card.toMutableList()
        val dataList = mutableListOf<String>()
        var aidType  = smartCardAidType
        dataList.addAll(smartCardDataList)
        dataList.addAll(ctlsCardDataList)
        isSuccess = false

        //Below For Loop is to set All AID of AID_SmartCard && CTLS_SmartCard by changing aidType:-
        for (i in 0 until dataList.size) {
            if (dataList[i].isEmpty()) {
                continue
            }

            if(i == AID_SmartCard.size){
                aidType = ctlsAidType
            }

            try {
                val emvParamAppUMS = EMVParamAppCaseA(AppPreference.getBankCode())
                emvParamAppUMS.setFlagAppendRemoveClear(ConstIPBOC.updateAID.operation.append)
                emvParamAppUMS.setAidType(aidType)
                emvParamAppUMS.append(dataList[i])
                isSuccess = ipboc!!.updateAID(
                    emvParamAppUMS.getFlagAppendRemoveClear(),
                    emvParamAppUMS.getAidType(),
                    emvParamAppUMS.tlvString
                )
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            Log.d("TAG", "" + isSuccess)
            if (isSuccess) {
                Log.d("TAG", "update AID success")
            } else {
                Log.e("TAG", "updateAID false")
            }
        }
    }

    /**
     * @brief set, update the RID
     *
     * In this demo, there're 2 way to set the AID
     * 1#, set each tag & value
     * 2#, set one tlv string
     * in the EMVParamKeyCaseA.append, you can reset the Tag or Value
     * \code{.java}
     * \endcode
     * @version
     * @see EMVParamKeyCaseA
     */
    fun setRID(type: Int) {
        var isSuccess: Boolean
        if (type == 3) {
            // clear RID
            isSuccess = false
            try {
                isSuccess = ipboc!!.updateRID(3, null)
                Log.d("TAG", "Clear RID :$isSuccess")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return
        }

        // way 2#, set one tlv string
        // hardcoding some rid
        val ridList = arrayOf(
            //Amex Test Cap keys
            /*   "9F0605A0000000259F2201C8DF0503201231DF060101DF070101DF028190BF0CFCED708FB6B048E3014336EA24AA007D7967B8AA4E613D26D015C4FE7805D9DB131CED0D2A8ED504C3B5CCD48C33199E5A5BF644DA043B54DBF60276F05B1750FAB39098C7511D04BABC649482DDCF7CC42C8C435BAB8DD0EB1A620C31111D1AAAF9AF6571EEBD4CF5A08496D57E7ABDBB5180E0A42DA869AB95FB620EFF2641C3702AF3BE0B0C138EAEF202E21DDF040103DF031433BD7A059FAB094939B90A8F35845C9DC779BD50", //c8
            "9F0605A0000000259F2201C9DF0503201231DF060101DF070101DF0281B0B362DB5733C15B8797B8ECEE55CB1A371F760E0BEDD3715BB270424FD4EA26062C38C3F4AAA3732A83D36EA8E9602F6683EECC6BAFF63DD2D49014BDE4D6D603CD744206B05B4BAD0C64C63AB3976B5C8CAAF8539549F5921C0B700D5B0F83C4E7E946068BAAAB5463544DB18C63801118F2182EFCC8A1E85E53C2A7AE839A5C6A3CABE73762B70D170AB64AFC6CA482944902611FB0061E09A67ACB77E493D998A0CCF93D81A4F6C0DC6B7DF22E62DBDF040103DF03148E8DFF443D78CD91DE88821D70C98F0638E51E49", //c9
            "9F0605A0000000259F2201CADF0503201231DF060101DF070101DF0281F8C23ECBD7119F479C2EE546C123A585D697A7D10B55C2D28BEF0D299C01DC65420A03FE5227ECDECB8025FBC86EEBC1935298C1753AB849936749719591758C315FA150400789BB14FADD6EAE2AD617DA38163199D1BAD5D3F8F6A7A20AEF420ADFE2404D30B219359C6A4952565CCCA6F11EC5BE564B49B0EA5BF5B3DC8C5C6401208D0029C3957A8C5922CBDE39D3A564C6DEBB6BD2AEF91FC27BB3D3892BEB9646DCE2E1EF8581EFFA712158AAEC541C0BBB4B3E279D7DA54E45A0ACC3570E712C9F7CDF985CFAFD382AE13A3B214A9E8E1E71AB1EA707895112ABC3A97D0FCB0AE2EE5C85492B6CFD54885CDD6337E895CC70FB3255E3DF040103DF03146BDA32B1AA171444C7E8F88075A74FBFE845765F", //CA,*/

            //Amex Live cap Keys
            "9F0605A000000025" + //Aid
                    "9F22010F" + //Key Id
                    "DF0503221231" + //Expiry Date
                    "DF060101" + // Hash Ind
                    "DF070101" + // ARITH_IND
                    "DF0281B0" + "C8D5AC27A5E1FB89978C7C6479AF993AB3800EB243996FBB2AE26B67B23AC482C4B746005A51AFA7D2D83E894F591A2357B30F85B85627FF15DA12290F70F05766552BA11AD34B7109FA49DE29DCB0109670875A17EA95549E92347B948AA1F045756DE56B707E3863E59A6CBE99C1272EF65FB66CBB4CFF070F36029DD76218B21242645B51CA752AF37E70BE1A84FF31079DC0048E928883EC4FADD497A719385C2BBBEBC5A66AA5E5655D18034EC5" + //Module
                    "DF040103" + //exponent
                    "DF0314A73472B3AB557493A9BC2179CC8014053B12BAB4",  //CheckSum

            //Amex Live cap Keys
            "9F0605A000000025" + //Aid
                    "9F220110" + //Key Id
                    "DF0503251231" + //Expiry Datef
                    "DF060101" + // Hash Ind
                    "DF070101" + // ARITH_IND
                    "DF0281F8" + "CF98DFEDB3D3727965EE7797723355E0751C81D2D3DF4D18EBAB9FB9D49F38C8C4A826B99DC9DEA3F01043D4BF22AC3550E2962A59639B1332156422F788B9C16D40135EFD1BA94147750575E636B6EBC618734C91C1D1BF3EDC2A46A43901668E0FFC136774080E888044F6A1E65DC9AAA8928DACBEB0DB55EA3514686C6A732CEF55EE27CF877F110652694A0E3484C855D882AE191674E25C296205BBB599455176FDD7BBC549F27BA5FE35336F7E29E68D783973199436633C67EE5A680F05160ED12D1665EC83D1997F10FD05BBDBF9433E8F797AEE3E9F02A34228ACE927ABE62B8B9281AD08D3DF5C7379685045D7BA5FCDE58637" + //Module
                    "DF040103" + //exponent
                    "DF0314C729CF2FD262394ABC4CC173506502446AA9B9FD",  //CheckSum

            //Visa Test cap Keys 92
            "9F0605A000000003" +
                    "9F220192" +
                    "DF050420291231" +
                    "DF0281B0996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9F" +
                    "DF040103" +
                    "DF0314429C954A3859CEF91295F663C963E582ED6EB253" +
                    "BF010131" +
                    "DF070101",
            //Visa Test cap Keys 94
            "9F0605A000000003" +
                    "9F220194" +
                    "DF050420291231" +
                    "DF0281F8ACD2B12302EE644F3F835ABD1FC7A6F62CCE48FFEC622AA8EF062BEF6FB8BA8BC68BBF6AB5870EED579BC3973E121303D34841A796D6DCBC41DBF9E52C4609795C0CCF7EE86FA1D5CB041071ED2C51D2202F63F1156C58A92D38BC60BDF424E1776E2BC9648078A03B36FB554375FC53D57C73F5160EA59F3AFC5398EC7B67758D65C9BFF7828B6B82D4BE124A416AB7301914311EA462C19F771F31B3B57336000DFF732D3B83DE07052D730354D297BEC72871DCCF0E193F171ABA27EE464C6A97690943D59BDABB2A27EB71CEEBDAFA1176046478FD62FEC452D5CA393296530AA3F41927ADFE434A2DF2AE3054F8840657A26E0FC617" +
                    "DF040103" + //exponent
                    "DF0314C4A3C43CCF87327D136B804160E47D43B60E6E0F" +
                    "BF010131" +
                    "DF070101",
            //Visa Test cap Keys 95
            "9F0605A000000003" +
                    "9F220195" +
                    "DF050420291231" +
                    "DF028190BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627BDF040103" +
                    "DF040103" + //exponent
                    "DF0314EE1511CEC71020A9B90443B37B1D5F6E703030F6" +
                    "BF010131" +
                    "DF070101",

            // MasterCard
            // MasterCard
            "9F0605A000000004" +
                    "9F2201EF" + //Test
                    "DF050420291231" +
                    "DF0281F8A191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3BDF040103DF031421766EBB0EE122AFB65D7845B73DB46BAB65427ABF010131DF070101",
            "9F0605A000000004" +
                    "9F2201FA" +
                    "DF050420291231DF028190A90FCD55AA2D5D9963E35ED0F440177699832F49C6BAB15CDAE5794BE93F934D4462D5D12762E48C38BA83D8445DEAA74195A301A102B2F114EADA0D180EE5E7A5C73E0C4E11F67A43DDAB5D55683B1474CC0627F44B8D3088A492FFAADAD4F42422D0E7013536C3C49AD3D0FAE96459B0F6B1B6056538A3D6D44640F94467B108867DEC40FAAECD740C00E2B7A8852DDF040103DF03142CFBB82409ED86A31973B0E0CEEA381BC43C8097BF010131DF070101",
            "9F0605A000000004" +
                    "9F220104" + //Live
                    "DF050420291231DF028190A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5DF040103DF0314381A035DA58B482EE2AF75F4C3F2CA469BA4AA6CBF010131DF070101",
            "9F0605A000000004" +
                    "9F220105" +//Test
                    "DF050420291231DF0281B0B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597DF040103DF0314EBFA0D5D06D8CE702DA3EAE890701D45E274C845BF010131DF070101",
            "9F0605A000000004" +
                    "9F220106" +//Live
                    "DF050420291231DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9BF010131DF070101",
            "9F0605A000000004" +
                    "9F2201F1" + //Test
                    "DF050420231231DF0281B0A0DCF4BDE19C3546B4B6F0414D174DDE294AABBB828C5A834D73AAE27C99B0B053A90278007239B6459FF0BBCD7B4B9C6C50AC02CE91368DA1BD21AAEADBC65347337D89B68F5C99A09D05BE02DD1F8C5BA20E2F13FB2A27C41D3F85CAD5CF6668E75851EC66EDBF98851FD4E42C44C1D59F5984703B27D5B9F21B8FA0D93279FBBF69E090642909C9EA27F898959541AA6757F5F624104F6E1D3A9532F2A6E51515AEAD1B43B3D7835088A2FAFA7BE7DF040103DF0314D8E68DA167AB5A85D8C3D55ECB9B0517A1A5B4BBBF010131DF070101",
            "9F0605A000000004" +
                    "9F220103" + //Live
                    "DF050420291231DF028180C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145DF040103DF03145ADDF21D09278661141179CBEFF272EA384B13BBBF010131DF070101",
            "9F0605A000000004" +
                    "9F220109" + //
                    "DF050420291231DF028180C132F436477A59302E885646102D913EC86A95DD5D0A56F625F472B67F52179BC8BD258A7CD43EF1720AC0065519E3FFCECC26F978EDF9FB8C6ECDF145FDCC697D6B72562FA2E0418B2B80A038D0DC3B769EB027484087CCE6652488D2B3816742AC9C2355B17411C47EACDD7467566B302F512806E331FAD964BF000169F641DF040103DF0300BF010131DF070101",

            //Diners Capk keys
            //  DINERS CAPK
            "9F0605A000000152" + // Aids
                    "9F22015A" + // key Id
                    "DF0506333131323138" + // exp date
                    "DF060101" +  // Hash Ind
                    "DF070101" + // ARith Index
                    "DF028180EDD8252468A705614B4D07DE3211B30031AEDB6D33A4315F2CFF7C97DB918993C2DC02E79E2FF8A2683D5BBD0F614BC9AB360A448283EF8B9CF6731D71D6BE939B7C5D0B0452D660CF24C21C47CAC8E26948C8EED8E3D00C016828D642816E658DC2CFC61E7E7D7740633BEFE34107C1FB55DEA7FAAEA2B25E85BED948893D07" + // Module
                    "DF040103" + // exponent
                    "DF0314CC9585E8E637191C10FCECB32B5AE1B9D410B52D",//Check Sum

            "9F0605A000000152" + // Aids
                    "9F22015C" + // Key Id
                    "DF050420291231" + //Expiry Date
                    "DF0281B0833F275FCF5CA4CB6F1BF880E54DCFEB721A316692CAFEB28B698CAECAFA2B2D2AD8517B1EFB59DDEFC39F9C3B33DDEE40E7A63C03E90A4DD261BC0F28B42EA6E7A1F307178E2D63FA1649155C3A5F926B4C7D7C258BCA98EF90C7F4117C205E8E32C45D10E3D494059D2F2933891B979CE4A831B301B0550CDAE9B67064B31D8B481B85A5B046BE8FFA7BDB58DC0D7032525297F26FF619AF7F15BCEC0C92BCDCBC4FB207D115AA65CD04C1CF982191" + // module
                    "DF040103" + //exponent
                    "DF0314C165C48EB36DDF969DDC0B326312AFE2F6B52713BF010131" +  // Checksum
                    "DF070101", // ARITH_IND

            "9F0605A000000152" + //Aids
                    "9F22015B" + //Key id
                    "DF050420291231" + //Expiry Date
                    "DF028190D3F45D065D4D900F68B2129AFA38F549AB9AE4619E5545814E468F382049A0B9776620DA60D62537F0705A2C926DBEAD4CA7CB43F0F0DD809584E9F7EFBDA3778747BC9E25C5606526FAB5E491646D4DD28278691C25956C8FED5E452F2442E25EDC6B0C1AA4B2E9EC4AD9B25A1B836295B823EDDC5EB6E1E0A3F41B28DB8C3B7E3E9B5979CD7E079EF024095A1D19DD" + // Module
                    "DF040103" + // exponent
                    "DF03140000000000000000000000000000000000000000BF010131" + // Checksum
                    "DF070101",// ARITH_IND

            "9F0605A000000152" + // Aids
                    "9F22015D" + // Key Id
                    "DF050420241231" + //Expiry Date
                    "DF0281F8AD938EA9888E5155F8CD272749172B3A8C504C17460EFA0BED7CBC5FD32C4A80FD810312281B5A35562800CDC325358A9639C501A537B7AE43DF263E6D232B811ACDB6DDE979D55D6C911173483993A423A0A5B1E1A70237885A241B8EEBB5571E2D32B41F9CC5514DF83F0D69270E109AF1422F985A52CCE04F3DF269B795155A68AD2D6B660DDCD759F0A5DA7B64104D22C2771ECE7A5FFD40C774E441379D1132FAF04CDF55B9504C6DCE9F61776D81C7C45F19B9EFB3749AC7D486A5AD2E781FA9D082FB2677665B99FA5F1553135A1FD2A2A9FBF625CA84A7D736521431178F13100A2516F9A43CE095B032B886C7A6AB126E203BE7" + // Module
                    "DF040103" + // exponent
                    "DF0314B51EC5F7DE9BB6D8BCE8FB5F69BA57A04221F39BBF010131" + // checksum
                    "DF070101"// ARITH_IND


        )
        try {
            isSuccess = ipboc!!.updateRID(3, null)
            Log.d("TAG", "Clear RID :$isSuccess")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        for (rid in ridList) {
            try {
                if (rid.isEmpty()) {
                    continue
                }
                val emvParamKeyUMS = EMVParamKeyCaseA()
                emvParamKeyUMS.append(rid)
                val bRet =
                    ipboc!!.updateRID(ConstIPBOC.updateRID.operation.append, rid)
                if (bRet) {
                    Log.d(TAG, "update RID success ")
                } else {
                    Log.e(TAG, "update RID fails ")
                }
            } catch (e: RemoteException) {
                Log.e(TAG, "update RID exception!")
                e.printStackTrace()
            }
        }
    }
}