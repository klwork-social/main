/*
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen.plugins.hibernate.predicates.relation;

import middlegen.predicates.relation.RelationshipRolePredicate;
import middlegen.RelationshipRole;
import org.apache.commons.collections.Predicate;

/**
 * Base class for column based predicates.
 *
 * @author Cesbron Sebastien and David Channon
 * @created 16 Oct 2003
 */
public class NotTargetInOriginPK extends RelationshipRolePredicate {

   /**
    * @todo-javadoc Describe the field
    */
   private final static Predicate _instance = new NotTargetInOriginPK();


   /**
    * Describe what the method does
    *
    * @param role Describe what the parameter does
    * @return Describe the return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    */
   public boolean evaluate(RelationshipRole role) {
      return !TargetInOriginPK.getInstance().evaluate(role);
   }


   /**
    * Gets the Instance attribute of the TargetOne class
    *
    * @return The Instance value
    */
   public static Predicate getInstance() {
      return _instance;
   }
}
