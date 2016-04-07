/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Gijs
 */
@FacesValidator("BsnValidator")
public class BsnValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            String input = value.toString();
            int bsn = 0;
            try {
                bsn = Integer.parseInt(input.replaceAll("[\\D]", ""));
            } catch(NumberFormatException ex) {
                FacesMessage msg = 
                        new FacesMessage("BSN validation failed.", 
                                        "Geen geldig BSN");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
            if(!isValidBSN(bsn)){
                FacesMessage msg = 
                        new FacesMessage("BSN validation failed.", 
                                        "Geen geldig BSN");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
	}
        
        private boolean isValidBSN(int candidate) {
            if (candidate <= 9999999 || candidate > 999999999) {
                return false;
            }
            int sum = -1 * candidate % 10;

            for (int multiplier = 2; candidate > 0; multiplier++) {
                int val = (candidate /= 10) % 10;
                sum += multiplier * val;
            }

            return sum != 0 && sum % 11 == 0;
        }
    
}
