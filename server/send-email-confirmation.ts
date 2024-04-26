import { NextApiRequest, NextApiResponse } from 'next';
import emailjs from '@emailjs/nodejs';

const FEYI_SERVICE = "service_p12emyx"
const FEYI_TEMPLATE = "template_h7ojaia"
const FEYI_PUBLIC_KEY = "t6p3E2z-1LtuoKSUf"

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'POST') {

      try {
        await emailjs.send(
            FEYI_SERVICE,
            FEYI_TEMPLATE,
            {},
            {
              publicKey: FEYI_PUBLIC_KEY,
            },
          );
        
        res.status(200).json({ success: true, message: 'Email sent successfully!' });
        
      } catch (error: any) {
        console.log(error.message)
        res.status(500).json({ error: error.message });
      }
    } else {
      res.setHeader('Allow', 'POST');
      res.status(405).end('Method Not Allowed');
    }
  }