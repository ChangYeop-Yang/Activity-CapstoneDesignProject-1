package com.health1st.yeop9657.health1st.ResourceData;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.CodeDt;

/**
 * Created by yeop on 2017. 10. 29..
 */

public class FHIRAdapter {

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : Patient */
    private Patient mPatient = null;

    /* MARK - : FHIR Adapter Creator */
    public FHIRAdapter(final Context mContext, final SharedPreferences mSharedPreferences) {
        this.mContext = mContext;
        createFHIR(mSharedPreferences);
    }

    /* MARK - : Get FHIR Document Method */
    public final String getFHIR() {

        final String sFHIRDocument = FhirContext.forDstu2().newJsonParser().setPrettyPrint(true).encodeResourceToString(mPatient);
        return sFHIRDocument.isEmpty() ? null : sFHIRDocument;
    }

    /* MARK - : Create FHIR Document Method */
    private final void createFHIR(final SharedPreferences mSharedPreferences) {

        /* POINT - : Patient Instance */
        mPatient = new Patient();
        mPatient.addIdentifier().setSystem("http://hl7.org/fhir/v2").setValue(UUID.randomUUID().toString());
        mPatient.setActive(true);

        mPatient.addName().setText(mSharedPreferences.getString(BasicData.SHARED_PATIENT_NAME, null));
        mPatient.setGender(AdministrativeGenderEnum.MALE);
        mPatient.setLanguage((CodeDt) new CodeDt().setValue("Ko-kr"));

        /* POINT - : Observation Instance */
        final Observation mObservation = new Observation();

        /* POINT - : FHIR BodySite */
        final Pair[] apBodyCodingDts = {new Pair("Weight", BasicData.SHARED_PATIENT_WEIGHT), new Pair("Height", BasicData.SHARED_PATIENT_HEIGHT),
                new Pair("Blood", BasicData.SHARED_PATIENT_BLOOD)};

        final ArrayList<CodingDt> mBodyList = new ArrayList<>(BasicData.ALLOCATE_BASIC_VALUE);
        for (final Pair<String, String> mPair : apBodyCodingDts) { mBodyList.add(new CodingDt().setCode(mPair.first).setDisplay(mSharedPreferences.getString(mPair.second, null))); }

        mObservation.setBodySite(new CodeableConceptDt().setCoding(mBodyList));


        /* POINT - : FHIR Personal  */
        final Pair[] apPersonalCodingDts = {new Pair("Age", BasicData.SHARED_PATIENT_AGE), new Pair("Smoke", BasicData.SHARED_PATIENT_SMOKE),
                new Pair("Disease", BasicData.SHARED_PATIENT_DISEASE), new Pair("Medicine", BasicData.SHARED_PATIENT_MEDICINE), new Pair("Etc", BasicData.SHARED_PATIENT_ETC)};

        final ArrayList<CodingDt> mPersonalList = new ArrayList<>(BasicData.ALLOCATE_BASIC_VALUE);
        for (final Pair<String, String> mPair : apPersonalCodingDts) { mPersonalList.add(new CodingDt().setCode(mPair.first).setDisplay(mSharedPreferences.getString(mPair.second, null))); }

        mObservation.setCategory(new CodeableConceptDt().setCoding(mPersonalList).setText("Personal Information"));

        mPatient.getManagingOrganization().setResource(mObservation);
    }
}
